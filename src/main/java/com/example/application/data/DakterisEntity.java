package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DakterisEntity.
 */
@Entity
@Table(name = "dakteris")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "dakteris")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DakterisEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "vards_uzvards_dakteris", nullable = false)
//    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vardsUzvardsDakteris;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DakterisEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVardsUzvardsDakteris() {
        return this.vardsUzvardsDakteris;
    }

    public DakterisEntity vardsUzvardsDakteris(String vardsUzvardsDakteris) {
        this.setVardsUzvardsDakteris(vardsUzvardsDakteris);
        return this;
    }

    public void setVardsUzvardsDakteris(String vardsUzvardsDakteris) {
        this.vardsUzvardsDakteris = vardsUzvardsDakteris;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DakterisEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((DakterisEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DakterisEntity{" +
            "id=" + getId() +
            ", vardsUzvardsDakteris='" + getVardsUzvardsDakteris() + "'" +
            "}";
    }
}
