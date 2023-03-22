package com.godcoder.myrest.service;

import com.godcoder.myrest.model.Role;
import com.godcoder.myrest.model.User;
import com.godcoder.myrest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
//        System.out.println("username = " + user.getUsername());
//        System.out.println("password = " + user.getPassword());
//        System.out.println("enabled = " + user.getEnabled());
        Role role = new Role();
        role.setId(1L);
        user.getRoles().add(role);
        return userRepository.save(user);
    }
}
