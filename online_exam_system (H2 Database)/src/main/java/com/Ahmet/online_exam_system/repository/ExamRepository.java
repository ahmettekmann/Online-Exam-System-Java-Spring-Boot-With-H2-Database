package com.Ahmet.online_exam_system.repository;

import com.Ahmet.online_exam_system.model.Exam;
import com.Ahmet.online_exam_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Exam findByName(String name);
    List<Exam> findByUserId(Long userId);
    List<Exam> findByDepartmentId(Long departmentId);
}
