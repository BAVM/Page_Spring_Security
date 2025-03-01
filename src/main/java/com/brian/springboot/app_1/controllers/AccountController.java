package com.brian.springboot.app_1.controllers;

import com.brian.springboot.app_1.Repositories.AppUserRepository;
import com.brian.springboot.app_1.models.AppUser;
import com.brian.springboot.app_1.models.RegisterDto;

import jakarta.validation.Valid;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
//@RestController
public class AccountController {

    @Autowired
    private AppUserRepository repo;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success",false);
        return "register";
    }

    @PostMapping("/register")
    public String register(
        Model model,
        @Valid @ModelAttribute RegisterDto registerDto,
        BindingResult result
        ){

            if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
                result.addError(new FieldError("registerDto", "confirmPassword", "Las contraseñas no coinciden"));
            }

            AppUser appUser = repo.findByEmail(registerDto.getEmail());
                if (appUser != null) {
                    result.addError(new FieldError("registerDto", "email", "El correo electrónico ya exite."));
                }

                if (result.hasErrors()) {
                    return "register";
                }

                try{

                    var bCryptEncoder = new BCryptPasswordEncoder();

                    AppUser newUser = new AppUser();
                    newUser.setFirstName(registerDto.getFirstName());
                    newUser.setLastName(registerDto.getLastName());
                    newUser.setEmail(registerDto.getEmail());
                    newUser.setPhone(registerDto.getPhone());
                    newUser.setAddress(registerDto.getAddress());
                    newUser.setRole("ROLE_USER");
                    newUser.setCreatedAt(new Date());
                    newUser.setPassword(bCryptEncoder.encode((registerDto.getPassword())));

                    repo.save(newUser);

                    model.addAttribute("registerDto", new RegisterDto());
                    model.addAttribute("success",true);

                }
                catch(Exception ex){
                    result.addError(new FieldError("registerDto", "firstName", ex.getMessage()));
                }

        return "register";
    }


}
