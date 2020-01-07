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
public class CompanyRESTController {
    private UserRepo userRepo;
    private AdminRepo adminRepo;
    private CompanyRepo companyRepo;

    public CompanyRESTController(UserRepo userRepo, AdminRepo adminRepo, CompanyRepo companyRepo) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.companyRepo = companyRepo;
    }

    @RequestMapping(value = "/companies_api/users",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<User> getUsers() {
        List<User> users = userRepo.findAll();
        users.addAll(adminRepo.findAll());

        return users;
    }

    @RequestMapping(value = "/companies_api/user",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public User getUser(@RequestParam("username") String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) user = adminRepo.findByUsername(username);

        return user;
    }

    @RequestMapping(value = "/companies_api/assign",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public User assignUserToCompany(@RequestParam("username") String username, @RequestParam("companyName") String companyName) {
        User user = userRepo.findByUsername(username);
        if (user == null) adminRepo.findByUsername(username);
        if (user == null) return null;

        user.setCompany(companyRepo.findByName(companyName));
        if (user instanceof Admin) return  adminRepo.save((Admin) user);
        return userRepo.save(user);
    }

    @RequestMapping(value = "/companies_api/assign_all",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<User> assignAllToCompany(@RequestBody List<String> usernames, @RequestParam("companyName") String companyName) {
        List<User> userList = new ArrayList<>();

        usernames.forEach(username -> {
            assignUserToCompany(username, companyName);

            User nextUser = userRepo.findByUsername(username);
            if (nextUser == null) nextUser = adminRepo.findByUsername(username);
            if (nextUser != null) userList.add(nextUser);
        });

        return userList;
    }

    @RequestMapping(value = "/companies_api/unassign",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public User unassignUser(@RequestParam("username") String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) adminRepo.findByUsername(username);
        if (user == null) return null;

        user.setCompany(null);
        if (user instanceof Admin) return adminRepo.save((Admin) user);
        return userRepo.save(user);
    }

    @RequestMapping(value = "/companies_api/unassign_all",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<User> unassignAll(@RequestBody List<String> usernames) {
        List<User> userList = new ArrayList<>();
        usernames.forEach(username -> {
            unassignUser(username);

            User nextUser = userRepo.findByUsername(username);
            if (nextUser == null) nextUser = adminRepo.findByUsername(username);
            if (nextUser != null) userList.add(nextUser);
        });

        return userList;
    }

    @RequestMapping(value = "/companies_api/delete",
            method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void deleteUser(@RequestParam("username") String username) {
        userRepo.deleteByUsername(username);
        adminRepo.deleteByUsername(username);
    }
}
