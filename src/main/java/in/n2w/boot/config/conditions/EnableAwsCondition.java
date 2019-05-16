package in.n2w.boot.config.conditions;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

/**
 * Created by Karanbir Singh on 5/16/2019.
 **/
public class EnableAwsCondition implements Condition {

    private final String ENABLE_AWS_KEY = "ENABLE_AWS";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        return env != null && env.getProperty(ENABLE_AWS_KEY).equals("true");
    }
}
