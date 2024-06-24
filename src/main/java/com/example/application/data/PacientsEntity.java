package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PacientsEntity.
 */
@Entity
@Table(name = "pacients")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class PacientsEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vards_uzvards_pacients", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vardsUzvardsPacients;

    @NotNull
    @Column(name = "personas_kods", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String personasKods;

    @NotNull
    @Column(name = "dzimsanas_gads", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer dzimsanasGads;

    }
