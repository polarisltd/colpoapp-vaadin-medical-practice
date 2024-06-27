package com.example.application.data;

import com.example.application.data.enumeration.ViziteAtkartojumsEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A KolposkopijaIzmeklejumsEntity.
 */
@Entity
@Table(name = "kolposkopija_izmeklejums")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "kolposkopijaizmeklejums")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KolposkopijaIzmeklejumsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    //@NotNull
    @Column(name = "izmeklejuma_nr", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String izmeklejumaNr;

    //@NotNull
    @Column(name = "izmeklejuma_datums", nullable = true)
    private Instant izmeklejumaDatums;

    @Enumerated(EnumType.STRING)
    @Column(name = "vizites_atkartojums")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ViziteAtkartojumsEnum vizitesAtkartojums;

    @Column(name = "skrininga_nr")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String skriningaNr;

    //@Lob
    @Column(name = "anamneze")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String anamneze;

    //@Lob
    @Column(name = "ieprieksh_veikta_terapija")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iepriekshVeiktaTerapija;

    @Column(name = "alergijas")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean alergijas;

    @Column(name = "trnsformacijas_zonas_tips")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String trnsformacijasZonasTips;

    //@NotNull
    @Column(name = "p_1", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p1;

    //@NotNull
    @Column(name = "p_2", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p2;

    //@NotNull
    @Column(name = "p_3", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p3;

    //@NotNull
    @Column(name = "p_4", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p4;

    //@NotNull
    @Column(name = "p_5", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p5;

    //@NotNull
    @Column(name = "m_1", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean m1;

    //@NotNull
    @Column(name = "m_2", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean m2;

    //@NotNull
    @Column(name = "m_3", nullable = true)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean m3;

    //@Lob
    @Column(name = "rezultati")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rezultati;

    //@Lob
    @Column(name = "sledziens")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sledziens;

    @Column(name = "nakosa_kolposkopijas_kontrole")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nakosaKolposkopijasKontrole;

    @ManyToOne(optional = false)
    //@NotNull
    private DakterisEntity dakteris;

    @ManyToOne(optional = false)
    //@NotNull
    private PacientsEntity pacients;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @OneToMany(mappedBy = "kolposkopijaIzmeklejumsEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageEntity> images = new ArrayList<>();


    }
