package com.vicchen.spring.boot.primary.replica.service;

import com.vicchen.spring.boot.primary.replica.entity.User;
import com.vicchen.spring.boot.primary.replica.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional()
    public User createUser(String name) {
        User user = new User();
        user.setName(name);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
