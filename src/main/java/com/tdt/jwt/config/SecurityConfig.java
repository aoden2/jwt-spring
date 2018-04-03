package com.tdt.jwt.config;

import com.tdt.jwt.service.JWTAuthenticationToken;
import com.tdt.jwt.service.JWTService;
import com.tdt.jwt.service.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author
 **/
@Slf4j
@Order(1)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String AUTH_HEADER = "X-AUTH";
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    UserAuthService userAuthService;

    @Autowired
    JWTService jwtService;
    @Autowired
    Environment environment;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder())
                .and()
                .authenticationProvider(new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

                        String jwtToken = (String) authentication.getCredentials();
                        authentication.setAuthenticated(jwtService.parseUserFromToken(jwtToken) != null);
                        SecurityContextHolder.getContext().setAuthentication(authentication); //important
                        return authentication;
                    }

                    @Override
                    public boolean supports(Class<?> authentication) {
                        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new AbstractAuthenticationProcessingFilter("/**") {

                    @Override
                    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
                        chain.doFilter(request, response);
                    }

                    @Override
                    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                            throws AuthenticationException, IOException, ServletException {

                        String jwtToken = request.getHeader(AUTH_HEADER);
                        try {

                            JWTAuthenticationToken jwtAuthenticationToken = new JWTAuthenticationToken(jwtToken, applicationContext);
                            return authenticationManager().authenticate(jwtAuthenticationToken);
                        } catch (Exception e) {
                            throw new com.tdt.jwt.exception.AuthenticationException(e.getMessage());
                        }
                    }
                }, BasicAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, auth) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .formLogin()
                .loginProcessingUrl("/auth")
                .successHandler((req, resp, auth) -> {
                    resp.setHeader(AUTH_HEADER, jwtService.createTokenForUser(auth.getPrincipal()));
                })
                .failureHandler((req, resp, auth) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}
