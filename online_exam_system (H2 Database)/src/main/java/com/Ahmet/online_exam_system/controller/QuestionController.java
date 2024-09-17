package com.Ahmet.online_exam_system.controller;

import com.Ahmet.online_exam_system.model.Exam;
import com.Ahmet.online_exam_system.model.Question;
import com.Ahmet.online_exam_system.repository.ExamRepository;
import com.Ahmet.online_exam_system.service.ExamService;
import com.Ahmet.online_exam_system.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRepository examRepository;

    @GetMapping("/soruekle")
    public String createQuestion (Model model) {
        model.addAttribute("question",new Question());
        return "soruekle";
    }

    @PostMapping("/soruekle")
    public String submitQuestions(@RequestBody List<Question> questions, HttpSession session) {
        Long examId = (Long) session.getAttribute("examId");
        Exam examData = examRepository.findById(examId).orElse(null);

        if (examData != null) {
            for (Question question : questions) {
                question.setExam(examData);
                questionService.saveQuestion(question);
            }
            return "redirect:/teacher/profile";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
}
