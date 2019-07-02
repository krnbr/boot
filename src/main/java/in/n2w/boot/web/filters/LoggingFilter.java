package in.n2w.boot.web.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Karanbir Singh on 6/16/2019.
 **/
@Component
public class LoggingFilter extends GenericFilterBean {

    private Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        logger.info(
            String.format(
                "Filter log for URI: %s%s %s",
                ((HttpServletRequest) request).getRequestURL().toString(),
                Optional.ofNullable(((HttpServletRequest) request).getQueryString()).map(value -> "?" + value).orElse(""),
                ((HttpServletRequest) request).getMethod()
            )
        );

        chain.doFilter(request, response);
        return;

    }

}
