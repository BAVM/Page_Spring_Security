package com.brian.springboot.app_1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
//@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/profile")
    public String adminProfile(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "admin/profile";
    }
}
