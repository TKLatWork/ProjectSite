package my.web.site.siteFrontend.config;

import my.web.site.commons.user.model.UserInfo;
import my.web.site.commons.user.vals.ConstValues;
import my.web.site.siteFrontend.user.MongoUserDetailsService;
import my.web.site.siteFrontend.user.api.UserApiCtrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static my.web.site.siteFrontend.user.api.UserApiCtrl.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MongoUserDetailsService mongoUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/*", "/static/**", "/spa/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //.antMatchers("/", "/public", "/public/**").permitAll()
                //all
                .antMatchers("/*/private/**")
                    .hasAnyAuthority(ConstValues.Role.Admin.toString(), ConstValues.Role.User.toString())
                //.antMatchers("/*/public/**").permitAll()
                //admin
                .antMatchers("/admin", "/admin/**", "/*/admin/**")
                    .hasAnyAuthority(ConstValues.Role.Admin.toString())
                .anyRequest()
                    .permitAll()
                    .and()
                //login and logout
                .formLogin()
                    .usernameParameter(UserInfo.F_USERNAME)
                    .passwordParameter(UserInfo.F_PASSWORD)
                    .loginProcessingUrl(BASE_URL + LOGIN)
                    .successForwardUrl(BASE_URL + LOGIN_SUCCESS)
                    //.failureHandler(failureHandler())
                    .failureForwardUrl(BASE_URL + LOGIN_FAIL)
                    .loginPage(BASE_URL + LOGIN_FAIL)
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl(BASE_URL + LOGOUT)
                    .logoutSuccessUrl(BASE_URL + LOGOUT_SUCCESS)
                    .permitAll()
                    .and()
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(1209600)
                    .and()
                //.anonymous().disable()
                .headers()
                    .frameOptions()
                    .disable()
                    .and()
                .cors().and()
                .csrf()
                .disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(mongoUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        InMemoryTokenRepositoryImpl repository = new InMemoryTokenRepositoryImpl();
        return repository;
    }

}
