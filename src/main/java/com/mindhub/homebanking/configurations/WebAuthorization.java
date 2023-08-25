package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/login", "api/logout").permitAll()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/scripts/**").permitAll()
                .antMatchers("/web/style/**").permitAll()
                .antMatchers("/web/images/**").permitAll()
                .antMatchers("/rest/**").hasAnyAuthority("ADMIN")
                .antMatchers("/h2-console/**").hasAnyAuthority("ADMIN")
                .antMatchers("/manager.html").hasAnyAuthority("ADMIN")
                .antMatchers("/api/clients").hasAnyAuthority("ADMIN")
                .antMatchers("/api/accounts").hasAnyAuthority("ADMIN")
                .antMatchers("/web/**").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/**").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts/{id}").hasAnyAuthority("CLIENT")
                .anyRequest().authenticated();
        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login")
                .permitAll();
        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
