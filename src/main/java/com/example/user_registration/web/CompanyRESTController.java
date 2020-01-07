package com.example.user_registration.web;

import com.example.user_registration.model.Admin;
import com.example.user_registration.model.User;
import com.example.user_registration.repo.AdminRepo;
import com.example.user_registration.repo.CompanyRepo;
import com.example.user_registration.repo.UserRepo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "companies_api", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class CompanyRESTController {
    private UserRepo userRepo;
    private AdminRepo adminRepo;
    private CompanyRepo companyRepo;

    public CompanyRESTController(UserRepo userRepo, AdminRepo adminRepo, CompanyRepo companyRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.companyRepo = companyRepo;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> users = userRepo.findAll();
        users.addAll(adminRepo.findAll());

        return users;
    }

    @GetMapping("/user")
    public User getUser(@RequestParam("username") String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) user = adminRepo.findByUsername(username);

        return user;
    }

    @PutMapping("/assign")
    public User assignUserToCompany(@RequestBody User user, @RequestParam("companyName") String companyName) {
        user.setCompany(companyRepo.findByName(companyName));
        if (userRepo.findByUsername(user.getUsername()) != null) userRepo.save(user);
        else if (adminRepo.findByUsername(user.getUsername()) != null) adminRepo.save((Admin) user);
        return user;
    }

    @PutMapping("/assign_all")
    public List<User> assignAllToCompany(@RequestBody List<User> users, @RequestParam("companyName") String companyName) {
        List<User> userList = new ArrayList<>();
        users.forEach(user -> {assignUserToCompany(user, companyName); userList.add(user);});

        return userList;
    }

    @PutMapping("/unassign")
    public User unassignUser(@RequestBody User user) {
        user.setCompany(null);
        if (userRepo.findByUsername(user.getUsername()) != null) userRepo.save(user);
        else if (adminRepo.findByUsername(user.getUsername()) != null) adminRepo.save((Admin) user);
        return user;
    }

    @PutMapping("/unassign_all")
    public List<User> unassignAll(@RequestBody List<User> users) {
        List<User> userList = new ArrayList<>();
        users.forEach(user -> {unassignUser(user); userList.add(user);});

        return userList;
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam("username") String username) {
        userRepo.deleteByUsername(username);
        adminRepo.deleteByUsername(username);
    }
}
