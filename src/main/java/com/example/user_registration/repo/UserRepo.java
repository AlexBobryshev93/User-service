package com.example.user_registration.repo;

import com.example.user_registration.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, Long> {
    User findByUsername(String username);
    void deleteByUsername(String username);
}
