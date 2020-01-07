package com.example.user_registration.web;

import com.example.user_registration.model.Company;
import com.example.user_registration.model.User;
import com.example.user_registration.repo.CompanyRepo;
import com.example.user_registration.repo.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CompanyRESTControllerTest {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    MockMvc mockMvc;

    private User user;
    private Company company;

    @Before
    public void setUp() throws Exception {
        company = new Company();
        company.setName("test_company");
        company.setForeign(false);
        companyRepo.save(company);

        user = new User();
        user.setUsername("test_user");
        user.setPassword("raw_password");
        userRepo.save(user);
    }

    @After
    public void tearDown() throws Exception {
        companyRepo.delete(company);
        userRepo.delete(user);
        company = null;
        user = null;
    }

    @Test
    public void getUsersTest() throws Exception {
        mockMvc.perform(get("/companies_api/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUserTest() throws Exception {
        mockMvc.perform(get("/companies_api/user?username=test_user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void assignUserToCompanyTest() throws Exception {
        mockMvc.perform(put("/companies_api/assign?username=test_user&companyName=test_company"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void assignAllToCompanyTest() throws Exception {
        mockMvc.perform(put("/companies_api/assign_all?companyName=test_company")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ArrayList<String>() {{add(user.getUsername());}})))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void unassignUserTest() throws Exception {
        mockMvc.perform(put("/companies_api/unassign?username=test_user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void unassignAllTest() throws Exception {
        mockMvc.perform(put("/companies_api/unassign_all")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(new ArrayList<String>() {{add(user.getUsername());}})))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/companies_api/delete?username=test_user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}