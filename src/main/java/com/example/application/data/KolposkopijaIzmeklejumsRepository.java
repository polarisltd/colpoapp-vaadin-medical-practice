package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KolposkopijaIzmeklejumsRepository extends JpaRepository<KolposkopijaIzmeklejumsEntity, Long> {

    @Query("SELECT k FROM KolposkopijaIzmeklejumsEntity k " +
            "JOIN FETCH k.pacients p " +
            "JOIN FETCH k.dakteris d " +
            "WHERE lower(p.vardsUzvardsPacients) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.personasKods) like lower(concat('%', :searchTerm, '%'))")
    List<KolposkopijaIzmeklejumsEntity> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT k FROM KolposkopijaIzmeklejumsEntity k JOIN FETCH k.pacients JOIN FETCH k.dakteris WHERE k.izmeklejumaDatums >= :startDate")
    List<KolposkopijaIzmeklejumsEntity> findAllByVisitDateAfter(LocalDate startDate);

    @Query("SELECT k FROM KolposkopijaIzmeklejumsEntity k JOIN FETCH k.pacients JOIN FETCH k.dakteris WHERE k.izmeklejumaDatums = :date")
    List<KolposkopijaIzmeklejumsEntity> findAllByIzmeklejumaDatums(LocalDate date);

    @Query("SELECT k FROM KolposkopijaIzmeklejumsEntity k JOIN FETCH k.pacients JOIN FETCH k.dakteris WHERE k.id = :id")
    Optional<KolposkopijaIzmeklejumsEntity> findByIdWithFetch(@Param("id") Long id);

    @Query("SELECT k FROM KolposkopijaIzmeklejumsEntity k JOIN FETCH k.pacients JOIN FETCH k.dakteris")
    List<KolposkopijaIzmeklejumsEntity> findAllWithFetch();


}
