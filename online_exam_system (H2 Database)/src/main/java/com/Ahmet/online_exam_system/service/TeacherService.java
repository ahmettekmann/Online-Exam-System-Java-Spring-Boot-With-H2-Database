package com.Ahmet.online_exam_system.service;

import com.Ahmet.online_exam_system.model.Student;
import com.Ahmet.online_exam_system.model.Teacher;
import com.Ahmet.online_exam_system.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {


    private final TeacherRepository teacherRepository;


    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;

    }

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }


    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    public Teacher getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }

}

