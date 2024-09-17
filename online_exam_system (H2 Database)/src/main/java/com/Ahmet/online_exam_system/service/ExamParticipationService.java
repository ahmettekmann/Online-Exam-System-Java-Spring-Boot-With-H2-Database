package com.Ahmet.online_exam_system.service;

import com.Ahmet.online_exam_system.model.ExamParticipation;
import com.Ahmet.online_exam_system.model.Exam;
import com.Ahmet.online_exam_system.model.User;
import com.Ahmet.online_exam_system.repository.ExamParticipationRepository;
import com.Ahmet.online_exam_system.repository.ExamRepository;
import com.Ahmet.online_exam_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExamParticipationService {

    @Autowired
    private ExamParticipationRepository examParticipationRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private UserRepository userRepository;


    public List<ExamParticipation> getAllExamParticipations() {
        return examParticipationRepository.findAll();
    }

    public Optional<ExamParticipation> getExamParticipationById(Long id) {
        return examParticipationRepository.findById(id);
    }

    public List<ExamParticipation> getExamParticipationsByStudentId(Long userId) {
        return examParticipationRepository.findByUserId(userId);
    }

    public List<ExamParticipation> getExamParticipationsByExamId(Long examId) {
        return examParticipationRepository.findByExamId(examId);
    }



    public ExamParticipation saveExamParticipation(Long examId, Long userId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid exam Id:" + examId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + userId));

        ExamParticipation participation = new ExamParticipation();
        participation.setExam(exam);
        participation.setUser(user);
        participation.setStartTime(LocalDateTime.now());

        return examParticipationRepository.save(participation);
    }
    public void save(ExamParticipation participation) {
        examParticipationRepository.save(participation);
    }

    public ExamParticipation updateExamParticipation(Long examId, Long userId, int points) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid exam Id:" + examId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + userId));

        Optional<ExamParticipation> optionalParticipation = examParticipationRepository.findByExamAndUser(exam, user);

        if (optionalParticipation.isPresent()) {
            ExamParticipation participation = optionalParticipation.get();
            participation.setEndTime(LocalDateTime.now());
            participation.setPoints(points);
            return examParticipationRepository.save(participation);
        } else {
            throw new EntityNotFoundException("Exam participation not found for given examId and studentId");
        }
    }
    public void deleteExamParticipation(Long id) {
        examParticipationRepository.deleteById(id);
    }
}
