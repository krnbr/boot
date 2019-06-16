package in.n2w.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
public class BootApplication {

    public static String APPLICATION_STRATEGY;

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
        SecurityContextHolder.setStrategyName(APPLICATION_STRATEGY);
    }

}
