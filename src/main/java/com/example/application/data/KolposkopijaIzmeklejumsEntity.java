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

    @Column(name = "vizites_atkartojums")
    private Boolean vizitesAtkartojums;

    @Column(name = "skrininga_nr")
    private String skriningaNr;

    //@Lob
    @Column(name = "anamneze")
    private String anamneze;

    //@Lob
    @Column(name = "ieprieksh_veikta_terapija_datums_rezultats")
    private String iepriekshVeiktaTerapija;

    @Column(name = "alergijas")
    private Boolean alergijas;

    @Column(name = "alergijas_comment")
    private String alergijasComment;

    @Column(name = "pm_pedeja_menesreize")
    private String pmPedejaMenesreize;

    @Column(name = "dzemdibu_skaits")
    private Integer dzemdibuSkaits;

    @Column(name = "pedeja_grutnieciba_gads")
    private String pedejaGrutniecibaGads;

    @Column(name = "kontracepcija")
    private Boolean kontracepcija;

    @Column(name = "kontracepcija_comment")
    private String kontracepcijaComment;

    @Column(name = "smeke")
    private Boolean smeke;

    @Column(name = "smeke_comment")
    private String smekeComment;

    @Column(name = "pedeja_citologiska_uztriepe_datums_rezultats")
    private String pedejaCitologiskaUztriepe;

    @Column(name = "hroniskas_saslimsanas_medikamentu_lietosana")
    private String hroniskasSaslimsanasMedikamentuLietosana;

    @Column(name = "kolposkopija_adekvata")
    private Boolean kolposkopijaAdekvata;

    @Column(name = "trnsformacijas_zonas_tips")
    private Integer trnsformacijasZonasTips;

    //@NotNull
    @Column(name = "p_1", nullable = true)
    private Integer p1;

    //@NotNull
    @Column(name = "p_2", nullable = true)
    private Integer p2;

    //@NotNull
    @Column(name = "p_3", nullable = true)
    private Integer p3;

    //@NotNull
    @Column(name = "p_4", nullable = true)
    private Integer p4;

    //@NotNull
    @Column(name = "p_5", nullable = true)
    private Integer p5;

    //@NotNull
    @Column(name = "m_1", nullable = true)
    private Boolean m1;

    //@NotNull
    @Column(name = "m_2", nullable = true)
    private Boolean m2;

    //@NotNull
    @Column(name = "m_3", nullable = true)
    private Boolean m3;

    @Column(name = "r_1", nullable = true)
    private Boolean r1;

    @Column(name = "r_2", nullable = true)
    private Boolean r2;

    @Column(name = "r_3", nullable = true)
    private Boolean r3;

    @Column(name = "r_4", nullable = true)
    private Boolean r4;

    @Column(name = "r_5", nullable = true)
    private Boolean r5;

    @Column(name = "r_6", nullable = true)
    private Boolean r6;

    @Column(name = "sledziens")
    private String sledziens;

    @Column(name = "nakosa_kolposkopijas_kontrole")
    private String nakosaKolposkopijasKontrole;

    @ManyToOne(optional = false)
    //@NotNull
    private DakterisEntity dakteris;

    @ManyToOne(optional = false)
    //@NotNull
    private PacientsEntity pacients;

//    @OneToMany(mappedBy = "kolposkopijaIzmeklejumsEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ImageEntity> images = new ArrayList<>();
@OneToMany(mappedBy = "kolposkopijaIzmeklejumsEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
private List<ImageEntity> images = new ArrayList<>();

    }
