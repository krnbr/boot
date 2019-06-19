package in.n2w.boot.web.controllers;

import in.n2w.boot.services.AsyncMockService;
import in.n2w.boot.services.RunAsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Karanbir Singh on 6/16/2019.
 **/
@RestController
@RequestMapping("/api/v1/run/as/")
public class RunAsController {

    @Autowired
    private RunAsService runAsService;

    @Autowired
    private AsyncMockService asyncMockService;

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

    @GetMapping("test")
    public String testMethod(@RequestParam(required = false, defaultValue = "Hello World") String message,
                             @RequestParam(required = false, defaultValue = "1000") Long wait){
        asyncMockService.someMock(message, wait);
        return "successfully tested";
    }

    /**
     * The endpoint /api/v1/run/as/current-user
     * Endpoint exposes the current user's authentication object's details
     * And This should include privilege based authorities
     * @return Authentication
     */
    @GetMapping("current-user")
    public Authentication notTestNotProtected(){
        return runAsService.getCurrentUserNotProtected();
    }

}
