package in.n2w.boot.web.controllers;

import in.n2w.boot.entities.User;
import in.n2w.boot.exceptions.EmailExistsException;
import in.n2w.boot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by Karanbir Singh on 4/16/2019.
 **/
@Controller
public class RegoController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("register", "user", new User());
    }

    @RequestMapping(value = "sign-up")
    public ModelAndView registerUser(@Valid final User user, final BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("register", "user", user);
        }
        try {
            userService.registerNewUser(user);
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("register", "user", user);
        }
        return new ModelAndView("redirect:/login");
    }

}
