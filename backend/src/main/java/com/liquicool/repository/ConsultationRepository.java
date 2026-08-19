package com.liquicool.repository;

import com.liquicool.entity.Consultation;
import com.liquicool.enums.ConsultationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Page<Consultation> findByUserId(Long userId, Pageable pageable);

    Page<Consultation> findByContactNameContainingIgnoreCaseOrPhoneContainingOrCompanyContaining(
            String contactName, String phone, String company, Pageable pageable);

    long countByStatus(ConsultationStatus status);

    @Query(value = "SELECT DATE(created_at) AS day, COUNT(*) AS cnt FROM consultation WHERE created_at >= ?1 GROUP BY DATE(created_at) ORDER BY day", nativeQuery = true)
    List<Object[]> countByDaySince(LocalDateTime startTime);
}
