package in.n2w.boot.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Karanbir Singh on 7/1/2019.
 **/
@RestController
@RequestMapping("/test/")
public class TestController {

    @GetMapping("abcd")
    public String testAbcd(){
        return "abcd";
    }

    @GetMapping("{one}/{two}")
    public String oneTwo(@PathVariable String one, @PathVariable String two){
        return one+"-"+two;
    }

}
