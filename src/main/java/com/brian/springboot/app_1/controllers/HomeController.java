package com.brian.springboot.app_1.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Controller
//@RestController
public class HomeController {

    @GetMapping({"","/"})
    public String home(){
        return "index";
    }

    @GetMapping({"contact"})
    public String contact(){
        return "contact";
    }



    @Controller
    //@RestController
    @RequestMapping("/profile")
    public class ProfileController {

        @GetMapping
        public String redirectProfile(Authentication authentication) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin/profile";
                } else if (authority.getAuthority().equals("ROLE_USER")) {
                    return "redirect:/user/profile";
                }
            }
            return "redirect:/login?error=role";
        }
    }


}


