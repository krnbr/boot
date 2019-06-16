package in.n2w.boot.config;

import in.n2w.boot.BootApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Karanbir Singh on 6/16/2019.
 **/
@Component
public class EnvironmentVarConfig {

    @Value("${application.security.strategy:MODE_THREADLOCAL}")
    public void setApplicationStrategy(String applicationStrategy) {
        BootApplication.APPLICATION_STRATEGY = applicationStrategy;
    }

}
