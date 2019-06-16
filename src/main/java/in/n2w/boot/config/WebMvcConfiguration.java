package in.n2w.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * Created by Karanbir Singh on 4/16/2019.
 **/
@Configuration
@EnableWebMvc
@EnableAsync
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${jsp.view.prefix}")
    private String JSP_VIEW_PREFIX;

    @Value("${jsp.view.suffix}")
    private String JSP_VIEW_SUFFIX;

    @Value("${jsp.view.view-names}")
    private String JSP_VIEW_NAMES;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/forgot-password").setViewName("forgot-password");

        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public ViewResolver viewResolver()
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix(JSP_VIEW_PREFIX);
        viewResolver.setViewNames(JSP_VIEW_NAMES);
        viewResolver.setCache(false);
        viewResolver.setSuffix(JSP_VIEW_SUFFIX);
        return viewResolver;
    }

}
