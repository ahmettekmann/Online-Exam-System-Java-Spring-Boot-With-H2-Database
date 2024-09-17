package com.Ahmet.online_exam_system.repository;

import com.Ahmet.online_exam_system.model.Exam;
import com.Ahmet.online_exam_system.model.ExamParticipation;
import com.Ahmet.online_exam_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamParticipationRepository extends JpaRepository<ExamParticipation, Long> {
    List<ExamParticipation> findByUserId(Long UserId);
    List<ExamParticipation> findByExamId(Long examId);
    ExamParticipation findByUserIdAndExamId(Long userId, Long examId);

    Optional<ExamParticipation> findByExamAndUser(Exam exam, User user);

}
