package com.example.user_registration.service;

import com.example.user_registration.model.User;
import com.example.user_registration.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRepoUserDetailsService implements UserDetailsService {
    private UserRepo userRepo;

    @Autowired
    public UserRepoUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities());
        }

        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }
}
