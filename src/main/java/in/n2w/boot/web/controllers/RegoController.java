package in.n2w.boot.web.controllers;

import com.google.common.collect.ImmutableMap;
import in.n2w.boot.entities.PasswordResetToken;
import in.n2w.boot.entities.SecurityQuestion;
import in.n2w.boot.entities.User;
import in.n2w.boot.entities.VerificationToken;
import in.n2w.boot.events.RegoCompleteEvent;
import in.n2w.boot.exceptions.EmailExistsException;
import in.n2w.boot.repositories.SecurityQuestionDefinitionRepository;
import in.n2w.boot.repositories.SecurityQuestionRepository;
import in.n2w.boot.security.SecureUserDetailsService;
import in.n2w.boot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Karanbir Singh on 4/16/2019.
 **/
@Controller
public class RegoController {

    private Logger logger = LoggerFactory.getLogger(RegoController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private SecureUserDetailsService userDetailsService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    SecurityQuestionDefinitionRepository securityQuestionDefinitionRepository;

    @Autowired
    SecurityQuestionRepository securityQuestionRepository;

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        final Map<String, Object> model = new HashMap<>();
        model.put("user", new User());
        model.put("questions", securityQuestionDefinitionRepository.findAll());
        return new ModelAndView("register", model);
    }

    @RequestMapping(value = "sign-up")
    public ModelAndView registerUser(@Valid final User user, final @RequestParam Long questionId, @RequestParam final String answer, final BindingResult result, final HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return new ModelAndView("register", "user", user);
        }
        try {
            final User registered = userService.registerNewUser(user);

            securityQuestionDefinitionRepository.findById(questionId).ifPresent(questionDefinition -> securityQuestionRepository.
                    save(new SecurityQuestion(user, questionDefinition, answer)));

            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new RegoCompleteEvent(registered, appUrl));
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "user", user);
        }
        redirectAttributes.addFlashAttribute("message", "You should receive a confirmation email shortly");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/registration-confirm")
    public ModelAndView confirmRegistration(final Model model, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid account confirmation token.");
            return new ModelAndView("redirect:/login");
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your registration token has expired. Please register again.");
            return new ModelAndView("redirect:/login");
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/user/reset-password", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail, final RedirectAttributes redirectAttributes) {
        final User user = userService.findUserByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
            mailSender.send(email);
        }

        redirectAttributes.addFlashAttribute("message", "You should receive an Password Reset Email shortly");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/user/change-password", method = RequestMethod.GET)
    public ModelAndView showChangePasswordPage(@RequestParam("id") final long id, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        final PasswordResetToken passToken = userService.getPasswordResetToken(token);
        if (passToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
            return new ModelAndView("redirect:/login");
        }
        final User user = passToken.getUser();
        if (user.getId() != id) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
            return new ModelAndView("redirect:/login");
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your password reset token has expired");
            return new ModelAndView("redirect:/login");
        }

        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return new ModelAndView("reset-password", ImmutableMap.of("questions", securityQuestionDefinitionRepository.findAll()));
    }

    @RequestMapping(value = "/user/save-password", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView savePassword(@RequestParam("password") final String password, @RequestParam("passwordConfirmation") final String passwordConfirmation, @RequestParam final Long questionId, @RequestParam final String answer,
                                     final RedirectAttributes redirectAttributes) {
        if (!password.equals(passwordConfirmation)) {
            final Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", "Passwords do not match");
            model.put("questions", securityQuestionDefinitionRepository.findAll());
            return new ModelAndView("reset-password", model);
        }
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (securityQuestionRepository.findByQuestionDefinitionIdAndUserIdAndAnswer(questionId, user.getId(), answer) == null) {
            final Map<String, Object> model = new HashMap<>();
            model.put("errorMessage", "Answer to security question is incorrect");
            model.put("questions", securityQuestionDefinitionRepository.findAll());
            return new ModelAndView("reset-password", model);
        }
        userService.changeUserPassword(user, password);
        redirectAttributes.addFlashAttribute("message", "Password reset successfully");
        return new ModelAndView("redirect:/login");
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/change-password?id=" + user.getId() + "&token=" + token;

        logger.info("url change-password -> "+url);

        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText("Please open the following URL to reset your password: \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
