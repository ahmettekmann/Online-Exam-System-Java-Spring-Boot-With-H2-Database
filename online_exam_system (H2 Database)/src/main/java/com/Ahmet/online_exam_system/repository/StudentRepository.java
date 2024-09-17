package com.Ahmet.online_exam_system.repository;

import com.Ahmet.online_exam_system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
  Student findByEmail(String email);
}
