package com.example.application.data;

import com.example.application.data.enumeration.ViziteAtkartojumsEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A KolposkopijaIzmeklejumsEntity.
 */
@Entity
@Table(name = "kolposkopija_izmeklejums")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "kolposkopijaizmeklejums")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KolposkopijaIzmeklejumsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "izmeklejuma_nr", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String izmeklejumaNr;

    @NotNull
    @Column(name = "izmeklejuma_datums", nullable = false)
    private Instant izmeklejumaDatums;

    @Enumerated(EnumType.STRING)
    @Column(name = "vizites_atkartojums")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ViziteAtkartojumsEnum vizitesAtkartojums;

    @Column(name = "skrininga_nr")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String skriningaNr;

    @Lob
    @Column(name = "anamneze", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String anamneze;

    @Lob
    @Column(name = "ieprieksh_veikta_terapija")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String iepriekshVeiktaTerapija;

    @Column(name = "alergijas")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean alergijas;

    @Column(name = "trnsformacijas_zonas_tips")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String trnsformacijasZonasTips;

    @NotNull
    @Column(name = "p_1", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p1;

    @NotNull
    @Column(name = "p_2", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p2;

    @NotNull
    @Column(name = "p_3", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p3;

    @NotNull
    @Column(name = "p_4", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p4;

    @NotNull
    @Column(name = "p_5", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean p5;

    @NotNull
    @Column(name = "m_1", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean m1;

    @NotNull
    @Column(name = "m_2", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean m2;

    @NotNull
    @Column(name = "m_3", nullable = false)
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean m3;

    @Lob
    @Column(name = "rezultati")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rezultati;

    @Lob
    @Column(name = "sledziens")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String sledziens;

    @Column(name = "nakosa_kolposkopijas_kontrole")
    //@org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nakosaKolposkopijasKontrole;

    @ManyToOne(optional = false)
    @NotNull
    private DakterisEntity dakteris;

    @ManyToOne(optional = false)
    @NotNull
    private PacientsEntity pacients;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KolposkopijaIzmeklejumsEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIzmeklejumaNr() {
        return this.izmeklejumaNr;
    }

    public KolposkopijaIzmeklejumsEntity izmeklejumaNr(String izmeklejumaNr) {
        this.setIzmeklejumaNr(izmeklejumaNr);
        return this;
    }

    public void setIzmeklejumaNr(String izmeklejumaNr) {
        this.izmeklejumaNr = izmeklejumaNr;
    }

    public Instant getIzmeklejumaDatums() {
        return this.izmeklejumaDatums;
    }

    public KolposkopijaIzmeklejumsEntity izmeklejumaDatums(Instant izmeklejumaDatums) {
        this.setIzmeklejumaDatums(izmeklejumaDatums);
        return this;
    }

    public void setIzmeklejumaDatums(Instant izmeklejumaDatums) {
        this.izmeklejumaDatums = izmeklejumaDatums;
    }

    public ViziteAtkartojumsEnum getVizitesAtkartojums() {
        return this.vizitesAtkartojums;
    }

    public KolposkopijaIzmeklejumsEntity vizitesAtkartojums(ViziteAtkartojumsEnum vizitesAtkartojums) {
        this.setVizitesAtkartojums(vizitesAtkartojums);
        return this;
    }

    public void setVizitesAtkartojums(ViziteAtkartojumsEnum vizitesAtkartojums) {
        this.vizitesAtkartojums = vizitesAtkartojums;
    }

    public String getSkriningaNr() {
        return this.skriningaNr;
    }

    public KolposkopijaIzmeklejumsEntity skriningaNr(String skriningaNr) {
        this.setSkriningaNr(skriningaNr);
        return this;
    }

    public void setSkriningaNr(String skriningaNr) {
        this.skriningaNr = skriningaNr;
    }

    public String getAnamneze() {
        return this.anamneze;
    }

    public KolposkopijaIzmeklejumsEntity anamneze(String anamneze) {
        this.setAnamneze(anamneze);
        return this;
    }

    public void setAnamneze(String anamneze) {
        this.anamneze = anamneze;
    }

    public String getIepriekshVeiktaTerapija() {
        return this.iepriekshVeiktaTerapija;
    }

    public KolposkopijaIzmeklejumsEntity iepriekshVeiktaTerapija(String iepriekshVeiktaTerapija) {
        this.setIepriekshVeiktaTerapija(iepriekshVeiktaTerapija);
        return this;
    }

    public void setIepriekshVeiktaTerapija(String iepriekshVeiktaTerapija) {
        this.iepriekshVeiktaTerapija = iepriekshVeiktaTerapija;
    }

    public Boolean getAlergijas() {
        return this.alergijas;
    }

    public KolposkopijaIzmeklejumsEntity alergijas(Boolean alergijas) {
        this.setAlergijas(alergijas);
        return this;
    }

    public void setAlergijas(Boolean alergijas) {
        this.alergijas = alergijas;
    }

    public String getTrnsformacijasZonasTips() {
        return this.trnsformacijasZonasTips;
    }

    public KolposkopijaIzmeklejumsEntity trnsformacijasZonasTips(String trnsformacijasZonasTips) {
        this.setTrnsformacijasZonasTips(trnsformacijasZonasTips);
        return this;
    }

    public void setTrnsformacijasZonasTips(String trnsformacijasZonasTips) {
        this.trnsformacijasZonasTips = trnsformacijasZonasTips;
    }

    public Boolean getp1() {
        return this.p1;
    }

    public KolposkopijaIzmeklejumsEntity p1(Boolean p1) {
        this.setp1(p1);
        return this;
    }

    public void setp1(Boolean p1) {
        this.p1 = p1;
    }

    public Boolean getp2() {
        return this.p2;
    }

    public KolposkopijaIzmeklejumsEntity p2(Boolean p2) {
        this.setp2(p2);
        return this;
    }

    public void setp2(Boolean p2) {
        this.p2 = p2;
    }

    public Boolean getp3() {
        return this.p3;
    }

    public KolposkopijaIzmeklejumsEntity p3(Boolean p3) {
        this.setp3(p3);
        return this;
    }

    public void setp3(Boolean p3) {
        this.p3 = p3;
    }

    public Boolean getp4() {
        return this.p4;
    }

    public KolposkopijaIzmeklejumsEntity p4(Boolean p4) {
        this.setp4(p4);
        return this;
    }

    public void setp4(Boolean p4) {
        this.p4 = p4;
    }

    public Boolean getp5() {
        return this.p5;
    }

    public KolposkopijaIzmeklejumsEntity p5(Boolean p5) {
        this.setp5(p5);
        return this;
    }

    public void setp5(Boolean p5) {
        this.p5 = p5;
    }

    public Boolean getm1() {
        return this.m1;
    }

    public KolposkopijaIzmeklejumsEntity m1(Boolean m1) {
        this.setm1(m1);
        return this;
    }

    public void setm1(Boolean m1) {
        this.m1 = m1;
    }

    public Boolean getm2() {
        return this.m2;
    }

    public KolposkopijaIzmeklejumsEntity m2(Boolean m2) {
        this.setm2(m2);
        return this;
    }

    public void setm2(Boolean m2) {
        this.m2 = m2;
    }

    public Boolean getm3() {
        return this.m3;
    }

    public KolposkopijaIzmeklejumsEntity m3(Boolean m3) {
        this.setm3(m3);
        return this;
    }

    public void setm3(Boolean m3) {
        this.m3 = m3;
    }

    public String getRezultati() {
        return this.rezultati;
    }

    public KolposkopijaIzmeklejumsEntity rezultati(String rezultati) {
        this.setRezultati(rezultati);
        return this;
    }

    public void setRezultati(String rezultati) {
        this.rezultati = rezultati;
    }

    public String getSledziens() {
        return this.sledziens;
    }

    public KolposkopijaIzmeklejumsEntity sledziens(String sledziens) {
        this.setSledziens(sledziens);
        return this;
    }

    public void setSledziens(String sledziens) {
        this.sledziens = sledziens;
    }

    public String getNakosaKolposkopijasKontrole() {
        return this.nakosaKolposkopijasKontrole;
    }

    public KolposkopijaIzmeklejumsEntity nakosaKolposkopijasKontrole(String nakosaKolposkopijasKontrole) {
        this.setNakosaKolposkopijasKontrole(nakosaKolposkopijasKontrole);
        return this;
    }

    public void setNakosaKolposkopijasKontrole(String nakosaKolposkopijasKontrole) {
        this.nakosaKolposkopijasKontrole = nakosaKolposkopijasKontrole;
    }

    public DakterisEntity getDakteris() {
        return this.dakteris;
    }

    public void setDakteris(DakterisEntity dakteris) {
        this.dakteris = dakteris;
    }

    public KolposkopijaIzmeklejumsEntity dakteris(DakterisEntity dakteris) {
        this.setDakteris(dakteris);
        return this;
    }

    public PacientsEntity getPacients() {
        return this.pacients;
    }

    public void setPacients(PacientsEntity pacients) {
        this.pacients = pacients;
    }

    public KolposkopijaIzmeklejumsEntity pacients(PacientsEntity pacients) {
        this.setPacients(pacients);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KolposkopijaIzmeklejumsEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((KolposkopijaIzmeklejumsEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KolposkopijaIzmeklejumsEntity{" +
            "id=" + getId() +
            ", izmeklejumaNr='" + getIzmeklejumaNr() + "'" +
            ", izmeklejumaDatums='" + getIzmeklejumaDatums() + "'" +
            ", vizitesAtkartojums='" + getVizitesAtkartojums() + "'" +
            ", skriningaNr='" + getSkriningaNr() + "'" +
            ", anamneze='" + getAnamneze() + "'" +
            ", iepriekshVeiktaTerapija='" + getIepriekshVeiktaTerapija() + "'" +
            ", alergijas='" + getAlergijas() + "'" +
            ", trnsformacijasZonasTips='" + getTrnsformacijasZonasTips() + "'" +
            ", p1='" + getp1() + "'" +
            ", p2='" + getp2() + "'" +
            ", p3='" + getp3() + "'" +
            ", p4='" + getp4() + "'" +
            ", p5='" + getp5() + "'" +
            ", m1='" + getm1() + "'" +
            ", m2='" + getm2() + "'" +
            ", m3='" + getm3() + "'" +
            ", rezultati='" + getRezultati() + "'" +
            ", sledziens='" + getSledziens() + "'" +
            ", nakosaKolposkopijasKontrole='" + getNakosaKolposkopijasKontrole() + "'" +
            "}";
    }
}
