package in.n2w.boot.web.controllers;

import in.n2w.boot.entities.UrlPermissions;
import in.n2w.boot.repositories.UserRepository;
import in.n2w.boot.security.CustomAntPathRequestMatcher;
import in.n2w.boot.services.UrlPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

/**
 * Created by Karanbir Singh on 6/28/2019.
 **/
@RestController
@RequestMapping("/v1/intercept/urls")
public class UrlInterceptorController {

    @Autowired
    private UrlPermissionsService urlPermissionsService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/",""})
    public List<UrlPermissions> getAll(){
        return urlPermissionsService.getAll();
    }

    @GetMapping("/delete")
    public void evictCache(final HttpServletResponse response){
        response.setStatus(SC_NO_CONTENT);
        urlPermissionsService.evictAllEntries();
    }

    @GetMapping("/get/user")
    public Collection getUser(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    @GetMapping("/get/users")
    public Collection getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/get/ui")
    public Collection getUrlInterceptors(){
        return urlPermissionsService.getAllRequestMatchers();
    }

    @GetMapping("/get/mp")
    public Map getUrlInterceptorsMap(){
        return urlPermissionsService.getAllRequestMatchersMap();
    }

    @GetMapping("/get/set")
    public Set<CustomAntPathRequestMatcher> getUrlInterceptorsSet(){
        return urlPermissionsService.getAllRequestMatchersSet();
    }

}
