package com.Ahmet.online_exam_system.repository;

import com.Ahmet.online_exam_system.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
