package com.softuni.angelovestates.config;

import com.softuni.angelovestates.repository.UserRepository;
//import com.softuni.angelovestates.service.ApplicationUserDetailsService;
import com.softuni.angelovestates.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UserRepository userRepository;
    private final ApplicationUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(UserRepository userRepository, ApplicationUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //TODO
                .authorizeHttpRequests()
                .antMatchers("/", "/contacts", "about-us", "/api/**", "/agents/**", "/offers/search/**", "/offers/details/**").permitAll()
                .antMatchers("/auth/register", "auth/login").permitAll()
                .antMatchers("/offers/makeActive/**", "offers/delete/**").authenticated()
                .antMatchers("/offers/add", "/user/**", "/reviews/add", "/auth/logout").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/auth/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
                    .failureHandler((request, response, exception) -> {
                    request.getSession().setAttribute("error", true);
                    response.sendRedirect("/auth/login");
                })
                .and()
                .logout()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/")
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                .permitAll()
                .and().headers().contentTypeOptions().disable()
                .and().csrf().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new ApplicationUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

}
