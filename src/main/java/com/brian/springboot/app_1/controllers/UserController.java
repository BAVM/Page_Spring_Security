package com.brian.springboot.app_1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/profile")
    public String userProfile(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "user/profile";
    }
}
