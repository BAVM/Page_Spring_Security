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
            .requestMatchers("/").permitAll()
            .requestMatchers("/contact").permitAll()
            .requestMatchers("/register").permitAll()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/admin/**").hasRole("admin")
            .requestMatchers("/user/**").hasRole("cliente")
            .requestMatchers("/logout").permitAll()
            .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(((request, response, authentication) -> {
                        authentication.getAuthorities().forEach(auth ->{
                            try{
                                if (auth.getAuthority().equals("admin")){
                                    response.sendRedirect("/admin/profile");
                                }else{
                                    response.sendRedirect("/user/profile");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        });
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
