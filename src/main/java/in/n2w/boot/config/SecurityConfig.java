package in.n2w.boot.config;

import in.n2w.boot.web.filters.LoggingFilter;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Created by Karanbir Singh on 4/10/2019.
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("secureUserDetailsService")
    UserDetailsService userDetailsService;

    @Value("${BASIC_TEXT_ENCRYPTOR_PASSWORD}")
    String BASIC_TEXT_ENCRYPTOR_PASSWORD;

    @Autowired
    private DataSource dataSource;

    public SecurityConfig(){
        super();
    }

    @Autowired
    private AuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private LoggingFilter loggingFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(loggingFilter, AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(
                        "/signup",
                        "/sign-up",
                        "/registration-confirm*",
                        "/forgot-password*",
                        "/user/reset-password*",
                        "/user/change-password*",
                        "/user/save-password*",
                        "/v1/aws/**").permitAll()
                .antMatchers("/v1/events/**").hasAnyRole("USER")
                .anyRequest().authenticated()

                .and()
                .formLogin().loginPage("/login").permitAll().loginProcessingUrl("/sign-in")


                .and()
                .logout().permitAll().logoutUrl("/logout")

                .and()
                .csrf().disable();

        http
                .rememberMe()
                .tokenValiditySeconds(604800)
                //.key("neuw-boot-sec")
                //.useSecureCookie(true)
                //.rememberMeCookieName("sticky-cookie")
                .rememberMeParameter("remember-user")
                .tokenRepository(persistentTokenRepository());



    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
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

    @Bean
    public AuthenticationProvider runAsAuthenticationProvider() {
        final RunAsImplAuthenticationProvider authProvider = new RunAsImplAuthenticationProvider();
        authProvider.setKey("access-as-user");
        return authProvider;
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //auth.authenticationProvider(daoAuthenticationProvider).authenticationProvider(runAsAuthenticationProvider());
    }

}
