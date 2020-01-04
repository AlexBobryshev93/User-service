package com.example.user_registration.web;

import com.example.user_registration.model.Admin;
import com.example.user_registration.model.User;
import com.example.user_registration.repo.AdminRepo;
import com.example.user_registration.repo.UserRepo;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    private UserRepo userRepo;
    private AdminRepo adminRepo;
    private PasswordEncoder passwordEncoder;
    private MongoOperations mongoOperations;

    public MainController(UserRepo userRepo, AdminRepo adminRepo, PasswordEncoder passwordEncoder, MongoOperations mongoOperations) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
        this.mongoOperations = mongoOperations;
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

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return "redirect:/login";
    }

    @GetMapping("/edit")
    public String editUser(Model model) {
        User user = userRepo.findByUsername(getCurrentUserUsername());
        if (user == null) user = adminRepo.findByUsername(getCurrentUserUsername());

        model.addAttribute("user", user);
        model.addAttribute("errMsg", "");
        return "edit_user";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("errMsg", "Error: check your password and try again");
            return "edit_user";
        }

        if (((userRepo.findByUsername(user.getUsername()) != null) || (adminRepo.findByUsername(user.getUsername()) != null))
            && (!user.getUsername().equals(getCurrentUserUsername()))
        ) {
            model.addAttribute("errMsg", "Error: user with such name already exists");
            return "edit_user";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Update update = new Update().set("username", user.getUsername()).set("password", user.getPassword());
        mongoOperations.updateFirst(new Query(Criteria.where("username").is(getCurrentUserUsername())), update, User.class);
        mongoOperations.updateFirst(new Query(Criteria.where("username").is(getCurrentUserUsername())), update, Admin.class);
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteProfile() {
        String username = getCurrentUserUsername();

        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        userRepo.deleteByUsername(username);
        adminRepo.deleteByUsername(username);

        return "redirect:/login";
    }

    private String getCurrentUserUsername() {
        return ((org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
