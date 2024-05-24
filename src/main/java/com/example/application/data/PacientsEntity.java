package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PacientsEntity.
 */
@Entity
@Table(name = "pacients")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "pacients")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PacientsEntity implements Serializable {

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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PacientsEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVardsUzvardsPacients() {
        return this.vardsUzvardsPacients;
    }

    public PacientsEntity vardsUzvardsPacients(String vardsUzvardsPacients) {
        this.setVardsUzvardsPacients(vardsUzvardsPacients);
        return this;
    }

    public void setVardsUzvardsPacients(String vardsUzvardsPacients) {
        this.vardsUzvardsPacients = vardsUzvardsPacients;
    }

    public String getPersonasKods() {
        return this.personasKods;
    }

    public PacientsEntity personasKods(String personasKods) {
        this.setPersonasKods(personasKods);
        return this;
    }

    public void setPersonasKods(String personasKods) {
        this.personasKods = personasKods;
    }

    public Integer getDzimsanasGads() {
        return this.dzimsanasGads;
    }

    public PacientsEntity dzimsanasGads(Integer dzimsanasGads) {
        this.setDzimsanasGads(dzimsanasGads);
        return this;
    }

    public void setDzimsanasGads(Integer dzimsanasGads) {
        this.dzimsanasGads = dzimsanasGads;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PacientsEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((PacientsEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PacientsEntity{" +
            "id=" + getId() +
            ", vardsUzvardsPacients='" + getVardsUzvardsPacients() + "'" +
            ", personasKods='" + getPersonasKods() + "'" +
            ", dzimsanasGads=" + getDzimsanasGads() +
            "}";
    }
}
