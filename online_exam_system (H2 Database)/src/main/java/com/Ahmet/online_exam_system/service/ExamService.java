package com.Ahmet.online_exam_system.service;

import com.Ahmet.online_exam_system.model.Exam;
import com.Ahmet.online_exam_system.model.Question;
import com.Ahmet.online_exam_system.repository.AnswerRepository;
import com.Ahmet.online_exam_system.repository.ExamRepository;
import com.Ahmet.online_exam_system.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }


    public Exam saveExam(Exam exam) {
        return examRepository.save(exam);
    }


    @Transactional
    public void deleteExam(Long examId) {
        List<Question> questions = questionRepository.findByExamId(examId);
        for (Question question : questions) {
            answerRepository.deleteByQuestionId(question.getId());
        }
        examRepository.deleteById(examId);
    }
}

