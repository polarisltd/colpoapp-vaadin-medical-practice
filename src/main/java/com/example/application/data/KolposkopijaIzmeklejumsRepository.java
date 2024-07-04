package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KolposkopijaIzmeklejumsRepository extends JpaRepository<KolposkopijaIzmeklejumsEntity, Long> {

    @Query(value = "select c.* from kolposkopija_izmeklejums c " +
            "join pacients p on c.pacients_id = p.id " +
            //"where lower(c.izmeklejuma_nr) like lower(concat('%', :searchTerm, '%')) " +
            //"or lower(c.nakosa_kolposkopijas_kontrole) like lower(concat('%', :searchTerm, '%')) " +
            "where lower(p.vards_uzvards_pacients) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.personas_kods) like lower(concat('%', :searchTerm, '%')) " +
            "or to_char(c.izmeklejuma_datums at time zone 'UTC', 'YYYYMMDD') like lower(concat('%', :searchTerm, '%'))",
            nativeQuery = true)
    List<KolposkopijaIzmeklejumsEntity> search(@Param("searchTerm") String searchTerm);
}
