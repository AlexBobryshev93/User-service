package com.example.user_registration;

import com.example.user_registration.model.Admin;
import com.example.user_registration.model.Company;
import com.example.user_registration.repo.AdminRepo;
import com.example.user_registration.repo.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserRegistrationApplication {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(UserRegistrationApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(AdminRepo adminRepo, CompanyRepo companyRepo) {
        return args -> {
            if (companyRepo.findByName("APPLICATION_COMMAND") == null) {
                Company company = new Company();
                company.setName("APPLICATION_COMMAND");
                company.setForeign(false);
                companyRepo.save(company);
            }

            if (adminRepo.findAll().isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setCompany(companyRepo.findByName("APPLICATION_COMMAND"));
                adminRepo.save(admin);
            }
        };
    }

    //remove _class
    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory,
                                       MongoMappingContext context) {

        MappingMongoConverter converter =
                new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return new MongoTemplate(mongoDbFactory, converter);
    }
}
