package in.n2w.boot.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Karanbir Singh on 4/6/2019.
 **/
@Controller
public class MainController {

    @GetMapping({"/","/home","index"})
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title","Boot Home");
        modelAndView.setViewName("home");
        return modelAndView;
    }

}
