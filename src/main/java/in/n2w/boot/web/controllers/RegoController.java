package in.n2w.boot.web.controllers;

import in.n2w.boot.entities.User;
import in.n2w.boot.entities.VerificationToken;
import in.n2w.boot.events.RegoCompleteEvent;
import in.n2w.boot.exceptions.EmailExistsException;
import in.n2w.boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

/**
 * Created by Karanbir Singh on 4/16/2019.
 **/
@Controller
public class RegoController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("register", "user", new User());
    }

    @RequestMapping(value = "sign-up")
    public ModelAndView registerUser(@Valid final User user, final BindingResult result, final HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ModelAndView("register", "user", user);
        }
        try {
            final User registeredUser = userService.registerNewUser(user);
            final String appUrl =
                    request.getProtocol() + "://" +
                    request.getServerName() + ":" +
                    request.getServerPort() +
                    request.getContextPath();
            eventPublisher.publishEvent(new RegoCompleteEvent(registeredUser, appUrl));
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("register", "user", user);
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/registrationConfirm")
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

}
