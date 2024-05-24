package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KolposkopijaIzmeklejumsRepository extends JpaRepository<KolposkopijaIzmeklejumsEntity, Long> {

    @Query("select c from KolposkopijaIzmeklejumsEntity c " +
            "where lower(c.izmeklejumaNr) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.nakosaKolposkopijasKontrole) like lower(concat('%', :searchTerm, '%'))")
    List<Contact> search(@Param("searchTerm") String searchTerm);
}
