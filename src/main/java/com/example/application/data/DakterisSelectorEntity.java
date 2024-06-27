package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * A DakterisSelectorEntity.
 */

@Entity
@Table(name = "doctor_selector")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DakterisSelectorEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "selector_date", nullable = false)
    private LocalDate selectorDate;

    @PrePersist
    public void prePersist() {
        selectorDate = LocalDate.now();
    }

    @ManyToOne
    @JoinColumn(name = "dakteris_id", referencedColumnName = "id")
    private DakterisEntity dakterisEntity;



}
