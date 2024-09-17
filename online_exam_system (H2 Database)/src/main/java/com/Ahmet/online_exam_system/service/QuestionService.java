package com.Ahmet.online_exam_system.service;

import com.Ahmet.online_exam_system.model.Question;
import com.Ahmet.online_exam_system.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> findByExamId(Long examId) {
        return questionRepository.findByExamId(examId);
    }
    void updateQuestion(Long questionId, Question updatedQuestion, QuestionRepository questionRepository) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question existingQuestion = optionalQuestion.get();
            existingQuestion.setQuestionText(updatedQuestion.getQuestionText());
            existingQuestion.setQuestionType(updatedQuestion.getQuestionType());
            existingQuestion.setOption1(updatedQuestion.getOption1());
            existingQuestion.setOption2(updatedQuestion.getOption2());
            existingQuestion.setOption3(updatedQuestion.getOption3());
            existingQuestion.setOption4(updatedQuestion.getOption4());
            existingQuestion.setOption5(updatedQuestion.getOption5());
            existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            existingQuestion.setPoints(updatedQuestion.getPoints());
            questionRepository.save(existingQuestion);
        } else {
            throw new RuntimeException("Soru bulunamadı: " + questionId);
        }
    }
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }
    public Question findById(Long id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.orElse(null);
    }

    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}

