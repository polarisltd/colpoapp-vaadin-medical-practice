package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientsRepository extends JpaRepository<PacientsEntity, Long> {
    @Query("select c from PacientsEntity c " +
            "where lower(c.vardsUzvardsPacients) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.personasKods) like lower(concat('%', :searchTerm, '%'))")
    List<PacientsEntity> search(@Param("searchTerm") String searchTerm);

}
