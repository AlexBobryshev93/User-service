package com.example.user_registration.web;

import com.example.user_registration.model.User;
import com.example.user_registration.repo.AdminRepo;
import com.example.user_registration.repo.UserRepo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainControllerTest {
    private MainController mainController;
    private User user;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepo userRepo;
    @Mock
    private AdminRepo adminRepo;
    @Mock
    private Model model;
    @Mock
    private MongoOperations mongoOperations;

    @Before
    public void setUp() throws Exception {
        mainController = new MainController(userRepo, adminRepo, passwordEncoder, mongoOperations);
        user = new User();
        user.setUsername("user");
        user.setPassword("pass");
        user.setConfirmPassword("pass");
    }

    @After
    public void tearDown() throws Exception {
        mainController = null;
        user = null;
    }

    @Test
    @WithMockUser
    public void homePageTest() throws Exception{
        mainController.homePage(model);
    }

    @Test(expected = NullPointerException.class)
    @WithMockUser
    public void companyPageTest() {
        mainController.companyPage(model);
    }

    @Test
    public void toRegistrationPageTest() {
        mainController.toRegistrationPage(model);
    }

    @Test
    public void registerUserTest() {
        mainController.registerUser(user, model);
    }

    @Test
    @WithMockUser
    public void editUserTest() {
        mainController.editUser(model);
    }

    @Test
    @WithMockUser
    public void updateUserTest() {
        mainController.updateUser(user, model);
    }

    @Test
    @WithMockUser
    public void deleteProfileTest() {
        mainController.deleteProfile();
    }
}