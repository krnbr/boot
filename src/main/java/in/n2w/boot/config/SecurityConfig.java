package in.n2w.boot.config;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by Karanbir Singh on 4/10/2019.
 **/
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("secureUserDetailsService")
    UserDetailsService userDetailsService;

    @Value("${BASIC_TEXT_ENCRYPTOR_PASSWORD}")
    String BASIC_TEXT_ENCRYPTOR_PASSWORD;

    public SecurityConfig(){
        super();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .antMatchers("/signup", "/sign-up").permitAll()
                .antMatchers("/v1/events/**").hasAnyRole("USER")
                .anyRequest().authenticated()

                .and()
                .formLogin().loginPage("/login").permitAll().loginProcessingUrl("/sign-in")

                .and()
                .logout().permitAll().logoutUrl("/logout")

                .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BasicTextEncryptor basicTextEncryptor() {
        final BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(BASIC_TEXT_ENCRYPTOR_PASSWORD);
        return basicTextEncryptor;
    }

}