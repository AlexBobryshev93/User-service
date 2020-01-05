package com.example.user_registration.web;

import com.example.user_registration.model.Company;
import com.example.user_registration.repo.CompanyRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {
    private CompanyRepo companyRepo;

    public CompanyController(CompanyRepo companyRepo) {
        this.companyRepo = companyRepo;
    }

    @GetMapping
    public String listCompanies(Model model) {
        model.addAttribute("companiesList", companyRepo.findAll());
        return "companies";
    }

    @GetMapping("/add")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company());
        model.addAttribute("errMsg", "");
        return "create_company";
    }

    @PostMapping("/add")
    public String createCompany(@ModelAttribute("company") Company company, Model model) {
        if (companyRepo.findByName(company.getName()) != null) {
            model.addAttribute("errMsg", "Error: company with such name already exists");
            return "create_company";
        }

        companyRepo.save(company);
        return "redirect:/companies";
    }

    @GetMapping("/edit/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {
        model.addAttribute("company", companyRepo.findById(id));
        model.addAttribute("errMsg", "");
        return "edit_company";
    }

    @PostMapping("/edit/{id}")
    public String updateCompany(@ModelAttribute("company") Company company, Model model) {
        Company found = companyRepo.findByName(company.getName());

        if ((found != null) && (found.getId() != company.getId())) {
            model.addAttribute("errMsg", "Error: company with such name already exists");
            return "edit_company";
        }

        companyRepo.save(company);
        return "redirect:/companies";
    }

    // (!) deletes even if forbidden
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable("id") Long id) {
        companyRepo.deleteById(id);
        return "redirect:/companies";
    }
}
