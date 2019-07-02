package in.n2w.boot.security;

import in.n2w.boot.services.UrlPermissionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Karanbir Singh on 6/25/2019.
 **/
@Component
public class CustomUrlAccessDecisionVoter implements AccessDecisionVoter<FilterInvocation> {

    private Logger logger = LoggerFactory.getLogger(CustomUrlAccessDecisionVoter.class);

    @Autowired
    private UrlPermissionsService urlPermissionsService;

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, FilterInvocation filterInvocation, Collection<ConfigAttribute> attributes) {
        Set<CustomAntPathRequestMatcher> matchers = urlPermissionsService.getAllRequestMatchersSet();

        logger.info("matchers.size() -> "+matchers.size());
        logger.info("authentication.getAuthorities() -> "+authentication.getAuthorities());
        logger.info("getting all urls");

        if(matchers.stream().anyMatch(
                c -> c.matches(filterInvocation.getHttpRequest())
        )){
            logger.info("filterInvocation.getHttpRequest().getRequestURI() -> "+filterInvocation.getHttpRequest().getRequestURI());
            logger.info("filterInvocation.getHttpRequest().getMethod() -> "+filterInvocation.getHttpRequest().getMethod());

            CustomAntPathRequestMatcher customAntPathRequestMatcher = matchers.stream().filter(c -> c.matches(filterInvocation.getHttpRequest())).findFirst().get();
            customAntPathRequestMatcher.getAllowedAuthorities();

            if(logger.isDebugEnabled()) {
                Set s = authentication.getAuthorities().stream()
                        .filter(r -> {
                            System.out.println(customAntPathRequestMatcher.getAllowedAuthorities() + " -- " + r + " -- " + " " + customAntPathRequestMatcher.getAllowedAuthorities().contains(r.getAuthority()));
                            return customAntPathRequestMatcher.getAllowedAuthorities().contains(r.getAuthority());
                        })
                        .collect(Collectors.toSet());

                boolean b = s.size() > 0;
            }

            if(authentication.getAuthorities().stream()
                    .filter(r -> {
                        System.out.println(customAntPathRequestMatcher.getAllowedAuthorities() + " -- " + r + " -- " + " " + customAntPathRequestMatcher.getAllowedAuthorities().contains(r.getAuthority()));
                        return customAntPathRequestMatcher.getAllowedAuthorities().contains(r.getAuthority());
                    })
                    .collect(Collectors.toSet()).size()>0){
                return ACCESS_GRANTED;
            }else {
                return ACCESS_DENIED;
            }
        }

        return ACCESS_ABSTAIN;
    }
}
