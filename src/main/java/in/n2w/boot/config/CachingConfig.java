package in.n2w.boot.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Karanbir Singh on 6/27/2019.
 **/
@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("interceptedUrls", "antRequestMatchers","permissionInterceptedUrls", "permissionAntRequestMatchers");
    }

}
