package com.vicchen.spring.boot.primary.replica.repository;

import com.vicchen.spring.boot.primary.replica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
