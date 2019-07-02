package in.n2w.boot.services;

import in.n2w.boot.entities.UrlPermissions;
import in.n2w.boot.repositories.UrlPermissionsRepository;
import in.n2w.boot.security.CustomAntPathRequestMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Karanbir Singh on 7/1/2019.
 **/
@Service
public class UrlPermissionsService {

    private Logger logger = LoggerFactory.getLogger(UrlPermissionsService.class);

    @Autowired
    private UrlPermissionsRepository urlPermissionsRepository;

    @Cacheable(value = "permissionInterceptedUrls")
    public List<UrlPermissions> getAll() {
        return urlPermissionsRepository.findAll();
    }

    public Set<CustomAntPathRequestMatcher> getAllRequestMatchers() {
        List<UrlPermissions> urlPermissions = getAll();

        Map<String, List<UrlPermissions>> groupByPriceMap =
                urlPermissions.stream().collect(Collectors.groupingBy(
                        u -> u.getPk().getUrl()+"_"+u.getPk().getMethod().name()
                ));

        System.out.println(groupByPriceMap);

        return null;
    }

    public Map getAllRequestMatchersMap() {
        List<UrlPermissions> urlPermissions = getAll();

        Map<String, List<UrlPermissions>> groupByUrlAndMethod =
                urlPermissions.stream().collect(Collectors.groupingBy(
                        u -> u.getPk().getUrl()+"_"+u.getPk().getMethod().name()
                ));

        Set s = groupByUrlAndMethod.entrySet().stream().map(
                e -> e.getValue().stream().map(
                        v -> new CustomAntPathRequestMatcher(
                                v.getPk().getUrl(),
                                v.getPk().getMethod().name(),
                                e.getValue().stream().map(i -> v.getPk().getRole().getName()+"_"+i.getPk().getPrivilege().getName()).collect(Collectors.toSet())
                        )
                ).collect(Collectors.toSet())
        ).collect(Collectors.toSet());

        System.out.println(groupByUrlAndMethod);

        return groupByUrlAndMethod;
    }

    @Cacheable(value = "permissionAntRequestMatchers")
    public Set<CustomAntPathRequestMatcher> getAllRequestMatchersSet() {
        List<UrlPermissions> urlPermissions = getAll();

        Map<String, List<UrlPermissions>> groupByUrlAndMethod =
                urlPermissions.stream().collect(Collectors.groupingBy(
                        u -> u.getPk().getUrl()+"_"+u.getPk().getMethod().name()
                ));

        Set s = groupByUrlAndMethod.entrySet().stream().flatMap(
                e -> e.getValue().stream().map(
                        v -> new CustomAntPathRequestMatcher(
                                v.getPk().getUrl(),
                                v.getPk().getMethod().name(),
                                e.getValue().stream().map(i -> v.getPk().getRole().getName()+"_"+i.getPk().getPrivilege().getName()).collect(Collectors.toSet())
                        )
                )
        ).collect(Collectors.toSet());

        System.out.println(groupByUrlAndMethod);

        return s;
    }

    @CacheEvict("permissionAntRequestMatchers")
    public void evictAllEntries(){
        logger.info("entries for the -> permissionAntRequestMatchers requested for eviction");
    }

}
