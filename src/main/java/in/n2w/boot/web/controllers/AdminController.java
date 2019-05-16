package in.n2w.boot.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Karanbir Singh on 4/6/2019.
 **/
@Controller
@RequestMapping("/admin/")
public class AdminController {

    @RequestMapping({"/home","/dashboard",""})
    public ModelAndView dashboard(@RequestParam(required = false, defaultValue = "v1") String version){
        if(version.equals("v2")) {
            return new ModelAndView(
                    "admin/dashboard",
                    new ModelMap("title", "Admin | Dashboard")
            );
        }
        return new ModelAndView("jsp/admin/home");
    }

}
