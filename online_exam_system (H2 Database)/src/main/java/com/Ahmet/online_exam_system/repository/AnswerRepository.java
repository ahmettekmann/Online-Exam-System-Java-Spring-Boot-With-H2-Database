package com.Ahmet.online_exam_system.repository;

import com.Ahmet.online_exam_system.model.Answer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByExamIdAndUserId(Long examId, Long userId);
    @Transactional
    void deleteByQuestionId(Long questionId);

}
