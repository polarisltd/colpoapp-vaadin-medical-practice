package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorSelectorRepository extends JpaRepository<DakterisSelectorEntity, Long> {
    @Query("SELECT d FROM DakterisSelectorEntity d WHERE d.selectorDate = COALESCE((SELECT MAX(d2.selectorDate) FROM DakterisSelectorEntity d2 WHERE d2.selectorDate <= CURRENT_DATE), (SELECT MAX(d3.selectorDate) FROM DakterisSelectorEntity d3))")
    List<DakterisSelectorEntity> search();

}
