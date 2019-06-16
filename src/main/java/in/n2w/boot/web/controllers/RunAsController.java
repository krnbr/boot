package in.n2w.boot.web.controllers;

import in.n2w.boot.services.RunAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Karanbir Singh on 6/16/2019.
 **/
@RestController
@RequestMapping("/api/v1/run/as/")
public class RunAsController {

    @Autowired
    private RunAsService runAsService;

    @GetMapping("mock")
    @Secured({ "ROLE_USER", "RUN_AS_REPORTER" })
    public Authentication test(){
        return runAsService.getCurrentUser();
    }

    @GetMapping("notMock")
    @Secured({ "ROLE_USER" })
    public Authentication notTest(){
        return runAsService.getCurrentUser();
    }

}
