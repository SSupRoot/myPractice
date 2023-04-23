package com.example.myHome.service;

import com.example.myHome.model.Role;
import com.example.myHome.model.User;
import com.example.myHome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){

        // 저장하는 로직 추가 필요 service를 따로 뺀이유
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        Role role = new Role();
        role.setId(Long.valueOf(1)) ;
        user.getRoles().add(role);

        return userRepository.save(user);
    }
}
