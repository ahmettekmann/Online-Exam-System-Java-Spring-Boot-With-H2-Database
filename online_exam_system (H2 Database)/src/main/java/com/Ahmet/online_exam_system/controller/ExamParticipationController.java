package com.Ahmet.online_exam_system.controller;

import com.Ahmet.online_exam_system.model.ExamParticipation;
import com.Ahmet.online_exam_system.service.ExamParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exam-participations")
public class ExamParticipationController {

    @Autowired
    private ExamParticipationService examParticipationService;

    @GetMapping("/all")
    public ResponseEntity<List<ExamParticipation>> getAllExamParticipations() {
        List<ExamParticipation> examParticipations = examParticipationService.getAllExamParticipations();
        return ResponseEntity.ok(examParticipations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamParticipation> getExamParticipationById(@PathVariable Long id) {
        Optional<ExamParticipation> examParticipation = examParticipationService.getExamParticipationById(id);
        if (examParticipation.isPresent()) {
            return ResponseEntity.ok(examParticipation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ExamParticipation>> getExamParticipationsByStudentId(@PathVariable Long studentId) {
        List<ExamParticipation> examParticipations = examParticipationService.getExamParticipationsByStudentId(studentId);
        return ResponseEntity.ok(examParticipations);
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamParticipation>> getExamParticipationsByExamId(@PathVariable Long examId) {
        List<ExamParticipation> examParticipations = examParticipationService.getExamParticipationsByExamId(examId);
        return ResponseEntity.ok(examParticipations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamParticipation(@PathVariable Long id) {
        examParticipationService.deleteExamParticipation(id);
        return ResponseEntity.noContent().build();
    }
}
