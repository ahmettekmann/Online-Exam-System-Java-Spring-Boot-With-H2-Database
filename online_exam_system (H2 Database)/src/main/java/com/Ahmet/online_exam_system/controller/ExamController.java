package com.Ahmet.online_exam_system.controller;

import com.Ahmet.online_exam_system.model.*;
import com.Ahmet.online_exam_system.repository.*;
import com.Ahmet.online_exam_system.service.AnswerService;
import com.Ahmet.online_exam_system.service.ExamService;
import com.Ahmet.online_exam_system.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ExamController {

    @Autowired
    private ExamService examService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private
    QuestionService questionService;
    @Autowired
    private ExamParticipationRepository examParticipationRepository;


    @GetMapping("/view-exams")
    public String getAllExams(Model model, HttpSession session) {
        Long departmentId = (Long) session.getAttribute("departmentId");
        Long userId = (Long) session.getAttribute("userId");

        List<ExamParticipation> participations = examParticipationRepository.findByUserId(userId);
        Set<Long> participatedExamIds = new HashSet<>();
        for (ExamParticipation participation : participations) {
            participatedExamIds.add(participation.getExam().getId());
        }

        List<Exam> allExams = examRepository.findByDepartmentId(departmentId);
        List<Exam> availableExams = new ArrayList<>();
        for (Exam exam : allExams) {
            if (!participatedExamIds.contains(exam.getId())) {
                availableExams.add(exam);
            }
        }

        model.addAttribute("exam", availableExams);
        return "view-exams";
    }

    @PostMapping("/view-exams")
    public ResponseEntity<Void> setExamIdInSession(@RequestParam Long examId, HttpSession session) {
        session.setAttribute("examId", examId);
        Long userId = (Long) session.getAttribute("userId");
        ExamParticipation examParticipation = new ExamParticipation();
        examParticipation.setExam(examRepository.findById(examId).get());
        User userdata = userRepository.findById(userId).get();
        String name=userdata.getName();
        System.out.println("İSİM: "+ name);
        examParticipation.setUser(userdata);
        examParticipation.setStartTime(LocalDateTime.now());


        examParticipationRepository.save(examParticipation);
        System.out.println("Examid:" +examId);
        System.out.println("UswrID :"+ userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/take-an-exam")
    public String getExamQuestions(Model model, HttpSession session) {
        Long examId = (Long) session.getAttribute("examId");
        Long userId = (Long) session.getAttribute("userId");
        session.setAttribute("examId2",examId);
        session.setAttribute("userId2",userId);
        Optional<Exam> exam = examRepository.findById(examId);
        if (exam.isPresent()) {
            List<Question> questions = questionService.findByExamId(examId);
            model.addAttribute("exam", exam.get());
            model.addAttribute("questions", questions);

            System.out.println("examId" + examId);
            System.out.println("UserId" + userId);
            return "take-an-exam";
        } else {
            return "error";
        }
    }

    @PostMapping("/take-an-exam")
    public String submitExam(@RequestParam Map<String, String> allParams, Model model, HttpSession session) {
        int totalPoints = 0;
        Long examId = (Long) session.getAttribute("examId2");
        Long userId = (Long) session.getAttribute("userId2");
        User userdata = userRepository.findById(userId).orElse(null);
        Exam examdata = examRepository.findById(examId).orElse(null);

        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            String paramName = entry.getKey();
            if (paramName.startsWith("answer_")) {
                String questionId = paramName.split("_")[1];
                String userAnswer = entry.getValue();
                String correctAnswer = allParams.get("correctAnswer_" + questionId);
                int questionPoints = Integer.parseInt(allParams.get("points_" + questionId));
                Question questionForAnswer = questionRepository.findById(Long.parseLong(questionId)).orElse(null);

                Answer answer = new Answer();
                answer.setUser(userdata);
                answer.setQuestion(questionForAnswer);
                answer.setAnswerText(userAnswer);
                answer.setExam(examdata);

                if (userAnswer.equals(correctAnswer) && !questionForAnswer.getQuestionType().equals("klasik")) {
                    answer.setPoint(questionForAnswer.getPoints());
                    answer.setIsCorrect(true);
                    totalPoints+=questionPoints;
                }
                else if(!userAnswer.equals(correctAnswer)){
                    answer.setPoint(0);
                    answer.setIsCorrect(false);
                }

                answerRepository.save(answer);
            }
        }

        ExamParticipation participation = examParticipationRepository.findByUserIdAndExamId(userId, examId);
        if (participation != null) {
            participation.setPoints(totalPoints);
            participation.setEndTime(LocalDateTime.now());
            participation.setIsActive("Yes");
            examParticipationRepository.save(participation);
        }

        model.addAttribute("totalPoints", totalPoints);
        return "redirect:view-exams";
    }


    @GetMapping("/view-exam-results")
    public String viewExamResults(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<ExamParticipation> examResults = examParticipationRepository.findByUserId(userId);
        List<Map<String, Object>> results = new ArrayList<>();

        for (ExamParticipation participation : examResults) {
            Map<String, Object> result = new HashMap<>();
            result.put("examName", participation.getExam().getName());
            result.put("points", participation.getPoints());
            result.put("isActive",participation.getIsActive());
            result.put("userId",participation.getUser().getId());
            result.put("examId",participation.getExam().getId());

            results.add(result);
        }

        model.addAttribute("examResults", results);
        return "view-exam-results";
    }

    @GetMapping("/teacher-view-results")
    public String examResults(HttpSession session,Model model) {
        Long userId = (Long) session.getAttribute("userId");
        List<ExamParticipation> exams = examParticipationRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();
        for (ExamParticipation participation : exams) {
            if (participation.getExam().getUser().getId() == userId ){
                Map<String, Object> result = new HashMap<>();
                result.put("examName", participation.getExam().getName());
                result.put("userName", participation.getUser().getName());
                result.put("userSurname", participation.getUser().getSurname());
                result.put("userNumber", participation.getUser().getNumber());
                result.put("points", participation.getPoints());
                result.put("startTime",participation.getStartTime());
                result.put("endTime",participation.getEndTime());
                result.put("userId",participation.getUser().getId());
                result.put("examId",participation.getExam().getId());;
                result.put("isActive",participation.getIsActive());;
                results.add(result);
        }
    }
        model.addAttribute("examResults", results);
        return "teacher-view-results";
    }

    @GetMapping("/view-answers")
    public String viewAnswers(@RequestParam("examId") Long examId, @RequestParam("userId") Long userId, Model model) {
        List<Answer> answers = answerRepository.findByExamIdAndUserId(examId, userId);
        User user = userRepository.findById(userId).orElse(null);
        Exam exam = examRepository.findById(examId).orElse(null);

        model.addAttribute("answers", answers);
        model.addAttribute("exam",exam);
        model.addAttribute("user",user);
        return "view-answers";
    }

    @GetMapping("/exams/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        Optional<Exam> exam = examService.getExamById(id);
        if (exam.isPresent()) {
            return ResponseEntity.ok(exam.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/teacher/profile")
    public String showTeacherPage(Model model,HttpSession session) {

        Long userId = (Long)session.getAttribute("userId");
        User userdata = userRepository.findById(userId).orElse(null);
        model.addAttribute("user",userdata);
        return "teacher";
    }

    @GetMapping("/create-exam")
    public String showCreateExamForm(Model model) {
        model.addAttribute("exam", new Exam());
        model.addAttribute("departments", departmentRepository.findAll());
        return "create-exam";
    }

    @PostMapping("/create-exam")
    public String createExam(@ModelAttribute Exam exam, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            System.out.println("User ID not found in session.");
            return "error";
        }
        User userdata = userRepository.findById(userId).orElse(null);
        if (userdata == null) {
            System.out.println("User not found for ID: " + userId);
            return "error"; //
        }
        exam.setUser(userdata);
        examService.saveExam(exam);
        session.setAttribute("examId", exam.getId());
        return "redirect:soruekle"; //
    }

    @GetMapping("/teacher-view-exams")
    public String teacherViewExams(Model model,HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        List<Exam> exams = examRepository.findByUserId(userId);
        exams.sort(Comparator.comparing(Exam::getStartDateTime));
        model.addAttribute("exam", exams);
        return "teacher-view-exams"; //
    }

    @PostMapping("/teacher-view-exams")
    public ResponseEntity<Void> setExamIdInSessionn(@RequestParam Long examId, HttpSession session) {
        session.setAttribute("examId", examId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teacher/view/students/exams")
    public String teacherViewStudentsAnswersPage(HttpSession session,Model model) {
        Long userId = (Long) session.getAttribute("userId");
        List<ExamParticipation> exams = examParticipationRepository.findAll();
        List<Map<String, Object>> results = new ArrayList<>();
        for (ExamParticipation participation : exams) {
            if (participation.getExam().getUser().getId() == userId) {
                Map<String, Object> result = new HashMap<>();
                result.put("userName", participation.getUser().getName());
                result.put("userSurname", participation.getUser().getSurname());
                result.put("userNumber", participation.getUser().getNumber());
                result.put("examName", participation.getExam().getName());
                result.put("departmentName", participation.getExam().getDepartment().getName());
                result.put("studentId",participation.getUser().getId());
                result.put("examId",participation.getExam().getId());
                result.put("isActive",participation.getIsActive());

                results.add(result);
            }

            model.addAttribute("exams", results);
        }
        return "teacher-view-students-exams";

    }


    @GetMapping("/teacher/evaluate/classic-questions")
    public  String evaluateQuestionsPage(@RequestParam("examId") Long examId, @RequestParam("studentId") Long studentId, Model model){
        List<Answer> answers = answerRepository.findByExamIdAndUserId(examId,studentId);
        List<Map<String, Object>> classicquestions = new ArrayList<>();
        List<ExamParticipation> exams= examParticipationRepository.findAll();
        for (Answer answer : answers) {
            if (answer.getQuestion().getQuestionType().equals("klasik")){
                Map<String, Object> questonsMap= new HashMap<>();
                questonsMap.put("questionText", answer.getQuestion().getQuestionText());
                questonsMap.put("questionAnswer", answer.getAnswerText());
                questonsMap.put("correctAnswer",answer.getQuestion().getCorrectAnswer());
                questonsMap.put("questionPoint",answer.getQuestion().getPoints());
                questonsMap.put("questionId",answer.getId());
                questonsMap.put("answerId",answer.getId());


                classicquestions.add(questonsMap);
            }

        }
        model.addAttribute("examId", examId);
        model.addAttribute("studentId", studentId);

        model.addAttribute("question",classicquestions);
        return "evaluate-to-questions";
    }

    @PostMapping("/teacher/evaluate/classic-questions")
    public String evaluateQuestions(@RequestParam("examId") Long examId,
                                    @RequestParam("studentId") Long studentId,
                                    @RequestParam Map<String, String> params,
                                    @RequestParam("totalPoints") int totalPoints,
                                    @RequestParam("answerId") List<Long> answerId,
                                    @RequestParam("points") List<Integer> points,
                                    Model model) {
        for (int i = 0; i < answerId.size(); i++) {
            Answer answer = answerRepository.findById(answerId.get(i)).orElse(null);
            int point = (points.get(i));
            answer.setPoint(point);
            answerRepository.save(answer);

            examParticipationRepository.findByUserIdAndExamId(studentId,examId);

            model.addAttribute("totalPoints", totalPoints);
            System.out.println("Total Points: " + totalPoints); // Debug amacıyla

        }
        ExamParticipation exam = examParticipationRepository.findByUserIdAndExamId(studentId,examId);
        int examPoint = exam.getPoints();
        exam.setPoints(examPoint+totalPoints);
        exam.setIsActive("No");
        examParticipationRepository.save(exam);


        return "redirect:/teacher/view/students/exams";
    }

    @GetMapping("/delete/exam/{examId}")
    public String deleteExam(@PathVariable("examId") Long examId) {
        examService.deleteExam(examId);
        return "redirect:/teacher-view-exams";
    }

    @GetMapping ("/edit/exam-info/{examId}")
        public String editExamInformationPage(@PathVariable Long examId, HttpSession session,Model model){
        Optional<Exam> exam = examRepository.findById(examId);
        model.addAttribute("departments", departmentRepository.findAll());

        if (exam.isPresent()) {
            model.addAttribute("exam", exam.get());
            return "edit-exam-info";
        }
        else{
            return "error";
        }
    }

    @PostMapping("/edit/exam-info")
    public String editExamInformation(@ModelAttribute Exam exam, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            System.out.println("User ID not found in session.");
            return "error";
        }
        User userdata = userRepository.findById(userId).orElse(null);
        if (userdata == null) {
            System.out.println("User not found for ID: " + userId);
            return "error";
        }

        Optional<Exam> existingExamOpt = examRepository.findById(exam.getId());
        if (!existingExamOpt.isPresent()) {
            return "error";
        }
        Exam existingExam = existingExamOpt.get();

        existingExam.setName(exam.getName());
        existingExam.setStartDateTime(exam.getStartDateTime());
        existingExam.setEndDateTime(exam.getEndDateTime());
        existingExam.setDepartment(exam.getDepartment());
        existingExam.setUser(userdata);
        examService.saveExam(existingExam);
        session.setAttribute("examId", existingExam.getId());
        return "redirect:/teacher-view-exams";
    }


    @GetMapping("/organize-an-exam")
    public String viewExams(Model model, HttpSession session ){
        Long examId = (Long) session.getAttribute("examId");
        Optional<Exam> exam = examRepository.findById(examId);
        if (exam.isPresent()) {
            List<Question> questions = questionService.findByExamId(examId);
            model.addAttribute("exam", exam.get());
            model.addAttribute("questions", questions);
            return "organize-an-exam";

        } else {
            return "error";
        }
    }

    @GetMapping("/question/edit/{questionId}")
    public String showEditForm(@PathVariable("questionId") Long questionId, Model model) {
        Question question = questionService.getQuestionById(questionId).orElse(null);
        model.addAttribute("question", question);
        model.addAttribute("pageTitle", "Soruyu Düzenle (ID: " + questionId + ")");
        return "question-save";
    }

    @GetMapping("/delete/question/{questionId}")
    public String deleteQuestion(@PathVariable("questionId") Long questionId) {
        questionService.deleteQuestion(questionId);
        return "redirect:/organize-an-exam";
    }

        @GetMapping("/add/question")
        public String showNewForm(Model model) {
            model.addAttribute("question", new Question());
            model.addAttribute("pageTitle", "Yeni Soru Ekle");
            return "question-save";
        }

        @PostMapping("/question/save")
        public String saveQuestion(@ModelAttribute("question") Question question,
                                   @RequestParam Map<String, String> params, HttpSession session,Model model) {

            model.addAttribute("pageTitle", "Edit Question (ID: " + question.getId()+ ")");
            String questionType = question.getQuestionType();


            System.out.println("Received params: " + params);

            if ("coktan_secme".equals(questionType)) {
                question.setOption1(params.get("option1"));
                question.setOption2(params.get("option2"));
                question.setOption3(params.get("option3"));
                question.setOption4(params.get("option4"));
                question.setOption5(params.get("option5"));
                question.setCorrectAnswer(params.get("correctAnswer"));
            } else if ("klasik".equals(questionType)) {
                question.setOption1(null);
                question.setOption2(null);
                question.setOption3(null);
                question.setOption4(null);
                question.setOption5(null);
                question.setCorrectAnswer(params.get("correctAnswer"));
            } else if ("dogru_yanlis".equals(questionType)) {
                question.setOption1(null);
                question.setOption2(null);
                question.setOption3(null);
                question.setOption4(null);
                question.setOption5(null);
                question.setCorrectAnswer(params.get("correctAnswer"));
            }

            Long examId = (Long) session.getAttribute("examId");
            Exam examdata = examRepository.findById(examId).orElse(null);
            question.setExam(examdata);
            questionService.saveQuestion(question);

            return "redirect:/organize-an-exam";
        }

    @GetMapping("/get-question/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable Long questionId) {
        Question question = questionService.findById(questionId);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/update-question")
    public ResponseEntity<Void> updateQuestion(@ModelAttribute Question question) {
        questionService.saveQuestion(question);
        return ResponseEntity.ok().build();
    }
   /* @GetMapping("/organize-an-exam")
    public String viewExams(Model model, HttpSession session ){
        Long examId = (Long) session.getAttribute("examId");
        Optional<Exam> exam = examRepository.findById(examId);
        if (exam.isPresent()) {
            List<Question> questions = questionService.findByExamId(examId);
            model.addAttribute("exam", exam.get());
            model.addAttribute("questions", questions);

            return "organize-an-exam";
        } else {
            return "error";
        }
    }

    @PostMapping("/organize-an-exam")
    public String OrganizeExams (@RequestParam Long questionId, HttpSession session) {
        session.setAttribute("questionId",questionId);
        return "redirect:organize-an-exam";

    }

    @GetMapping("/update-question")
    public String updateQuestionPage (HttpSession session,Model model) {
        Long questionId = (Long) session.getAttribute("questionId");
        Question question = questionService.findById(questionId);
        model.addAttribute("question", question);
        return "update-question";
    }

    @PostMapping("/update-question")
    public String updateQuestion(@RequestParam("id") Long questionId,
                                 @RequestParam("questionText") String questionText,
                                 @RequestParam("questionType") String questionType,
                                 @RequestParam("points") int points,
                                 @RequestParam(value = "option1", required = false) String option1,
                                 @RequestParam(value = "option2", required = false) String option2,
                                 @RequestParam(value = "option3", required = false) String option3,
                                 @RequestParam(value = "option4", required = false) String option4,
                                 @RequestParam(value = "option5", required = false) String option5,
                                 @RequestParam(value = "correctAnswer", required = false) String correctAnswer) {
        Question question = questionService.findById(questionId);
        question.setQuestionText(questionText);
        question.setQuestionType(questionType);
        question.setPoints(points);
        if ("coktan_secme".equals(questionType)) {
            question.setOption1(option1);
            question.setOption2(option2);
            question.setOption3(option3);
            question.setOption4(option4);
            question.setOption5(option5);
            question.setCorrectAnswer(correctAnswer);
            questionService.saveQuestion(question);

        } if ("klasik".equals(questionType)) {
            question.setOption1(null);
            question.setOption2(null);
            question.setOption3(null);
            question.setOption4(null);
            question.setOption5(null);

            question.setCorrectAnswer(correctAnswer);
            questionService.saveQuestion(question);
        }  if ("dogru_yanlis".equals(questionType)) {
            question.setOption1(null);
            question.setOption2(null);
            question.setOption3(null);
            question.setOption4(null);
            question.setOption5(null);

            question.setCorrectAnswer(correctAnswer);
            questionService.saveQuestion(question);
        }
        return "redirect:/teacher";
    }


    @DeleteMapping("/exams/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    */

    }


