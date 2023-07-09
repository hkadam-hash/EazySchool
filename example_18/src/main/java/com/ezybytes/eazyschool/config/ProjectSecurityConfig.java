package com.ezybytes.eazyschool.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().ignoringRequestMatchers("/saveMsg").ignoringRequestMatchers(PathRequest.toH2Console());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("","/","/home").permitAll()
                .requestMatchers("/contact").permitAll()
                .requestMatchers("/holidays/**").permitAll()
                .requestMatchers("/saveMsg").permitAll()
                .requestMatchers("/courses").permitAll()
                .requestMatchers("/about").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/logout").permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/dashboard").authenticated()
            ).formLogin(form -> form.loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll()
                .failureUrl("/login?error=true")
                .permitAll()).logout(logout ->logout.logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true).permitAll());
        http.httpBasic(Customizer.withDefaults());
        http.headers().frameOptions().disable();
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService () {
        UserDetails user = User.withDefaultPasswordEncoder()
                    .username("user")
                    .password("12345")
                    .roles("USER")
                    .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("54321")
                .roles("USER","ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user,admin);
    }
}
