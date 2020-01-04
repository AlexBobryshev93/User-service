package com.example.user_registration.repo;

import com.example.user_registration.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepo extends MongoRepository<Admin, Long> {
    Admin findByUsername(String username);
    void deleteByUsername(String username);
}
