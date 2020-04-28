package net.dreamfteam.quiznet.configs.security;


import io.swagger.models.HttpMethod;
import net.dreamfteam.quiznet.configs.Constants;
import net.dreamfteam.quiznet.configs.token.JwtAuthenticationEntryPoint;
import net.dreamfteam.quiznet.configs.token.JwtConfigurer;
import net.dreamfteam.quiznet.configs.token.JwtTokenProvider;
import net.dreamfteam.quiznet.service.impl.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsUtils;

/**
 * Main security configuration
 *
 * @author Yevhen Khominich
 * @version 1.0
 */


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private JwtUserDetailsService jwtUserDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationEntryPoint unauthorizedHandler, JwtUserDetailsService jwtUserDetailsService,
                                 BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(jwtUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers(
                        Constants.SECUR_SIGN_UP_URLS,
                        Constants.SECUR_LOG_IN_URLS,
                        Constants.SECUR_ACTIVATION_URLS,
                        Constants.SECUR_RECOVER_URLS,
                        Constants.SECUR_QUIZ_QUESTION_LIST_URLS,
                        Constants.SECUR_QUIZ_TOTAL_SIZE_URLS,
                        Constants.SECUR_QUIZ_LIST_URLS,
                        Constants.SECUR_QUIZ_URLS,
                        Constants.SECUR_QUIZ_CATEG_LIST_URLS,
                        Constants.SECUR_QUIZ_TAG_LIST_URLS,
                        Constants.SECURE_ANNOUNCEMENT_LIST_URLS,
                        Constants.SECURE_ANNOUNCEMENT_URLS,
                        //for Swagger
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**"

                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()// off httpBasic
                .csrf().disable()     // off csrf
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)// add Exception Handler
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // without session
                .and()
                .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated()//other URLS only authenticated( with token)
                .and()
                .anonymous()
                .and()
                .cors()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));

        //http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

    }

}
