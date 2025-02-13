package com.brian.springboot.app_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/").permitAll()
            .requestMatchers("/contact").permitAll()
            .requestMatchers("/register").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/admin/profile").hasRole("ADMIN")
            .requestMatchers("/user/profile").hasRole("USER")
            .requestMatchers("/logout").permitAll()
            .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(((request, response, authentication) -> {
                        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                            response.sendRedirect("/admin/profile");
                        } else if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))){
                            response.sendRedirect("/user/profile");
                        }else {
                            response.sendRedirect("/login?error=role");
                        }
                    }))
                    //.defaultSuccessUrl("/",true)
            )
            .logout(config -> config.logoutSuccessUrl("/"))
            .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        /*MOSTRAR LA CONTASEÑA ENCRIPTADA */
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("password");
        System.out.println(encodedPassword);

        return new BCryptPasswordEncoder();
    }

}
