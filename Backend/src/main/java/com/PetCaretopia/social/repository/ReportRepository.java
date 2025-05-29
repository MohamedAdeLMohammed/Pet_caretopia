package com.PetCaretopia.social.repository;

import com.PetCaretopia.social.entity.Report;
import com.PetCaretopia.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReporter(User reporter);
}
