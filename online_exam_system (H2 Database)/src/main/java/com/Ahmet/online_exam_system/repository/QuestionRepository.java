package com.Ahmet.online_exam_system.repository;

import com.Ahmet.online_exam_system.model.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByExamId(Long examId);

    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.correctAnswer = NULL WHERE q.id = :id")
    void clearCorrectAnswer(Long id);
}
