package com.example.user_registration.web;

import com.example.user_registration.model.User;
import com.example.user_registration.repo.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public MainController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signup")
    public String toRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("errMsg", "");
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("errMsg", "Error: check your password and try again");
            return "signup";
        }

        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errMsg", "Error: user with such name already exists");
            return "signup";
        }

        //System.out.println(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(0l); // should be generated
        userRepo.save(user);

        return "redirect:/login";
    }
}
