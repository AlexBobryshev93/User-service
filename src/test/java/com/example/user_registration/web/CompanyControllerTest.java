package com.example.user_registration.web;

import com.example.user_registration.model.Company;
import com.example.user_registration.repo.CompanyRepo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(roles = "ADMIN")
public class CompanyControllerTest {
    private CompanyController companyController;
    private Company company;

    @Mock
    private CompanyRepo companyRepo;
    @Mock
    private Model model;

    @Before
    public void setUp() throws Exception {
        companyController = new CompanyController(companyRepo);
        company = new Company();
        company.setName("test");
        company.setForeign(true);
    }

    @After
    public void tearDown() throws Exception {
        companyController = null;
        company = null;
    }

    @Test
    public void listCompaniesTest() {
        companyController.listCompanies(model);
    }

    @Test
    public void addCompanyTest() {
        companyController.addCompany(model);
    }

    @Test
    public void createCompanyTest() {
        companyController.createCompany(company, model);
    }

    @Test
    public void editCompanyTest() {
        companyController.editCompany(-1L, model);
    }

    @Test
    public void updateCompanyTest() {
        companyController.updateCompany(company, model);
    }

    @Test
    public void deleteCompanyTest() {
        companyController.deleteCompany(-1L);
    }
}