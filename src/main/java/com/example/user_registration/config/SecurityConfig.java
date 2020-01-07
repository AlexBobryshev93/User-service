package com.example.user_registration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("059cr3t");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/signup").permitAll()
                .antMatchers("/", "/edit", "/delete").access("hasRole('ROLE_USER')")
                .antMatchers("/companies", "/companies/*").access("hasRole('ROLE_ADMIN')")
        .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
        .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
        .and()
            .csrf()
                .ignoringAntMatchers("/signup", "/edit", "/companies/add", "/companies/edit/*", "/companies_api/*");
    }
}
