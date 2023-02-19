package com.dzhatdoev.myinterviewtask.config;

import com.dzhatdoev.myinterviewtask.security.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(personDetailsService);
        return auth.build();
    }

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() throws Exception {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/login", "/error", "/auth/registration", "/quotes", "/quotes/top", "/quotes/flop").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/quotes", true)
                //в случае ошибки обратно на страницу логина
                .failureUrl("/auth/login?error")
                //Обеспечиваем возможность выхода
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");
        return http.build();
    }
}

