package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DakterisRepository extends JpaRepository<DakterisEntity, Long> {
    @Query("select c from DakterisEntity c " +
            "where lower(c.vardsUzvardsDakteris) like lower(concat('%', :searchTerm, '%')) ")
    List<DakterisEntity> search(@Param("searchTerm") String searchTerm);
}
