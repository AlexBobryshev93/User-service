package com.example.user_registration.repo;

import com.example.user_registration.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepo extends MongoRepository<Company, Long> {
    Company findByName(String name);
}
