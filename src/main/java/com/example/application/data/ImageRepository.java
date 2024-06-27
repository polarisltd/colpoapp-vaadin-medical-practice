package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    @Query("select c from ImageEntity c " +
            "where c.visitId = :visitId")
    List<ImageEntity> findByVisitId(@Param("visitId") int visitId);
}
