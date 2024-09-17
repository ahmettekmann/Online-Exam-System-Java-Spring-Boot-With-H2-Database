package com.Ahmet.online_exam_system.repository;

import com.Ahmet.online_exam_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);


}
