package ar.edu.um.isa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_use")
    private LocalDate lastUse;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Publication> publications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tag name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getLastUse() {
        return lastUse;
    }

    public Tag lastUse(LocalDate lastUse) {
        this.lastUse = lastUse;
        return this;
    }

    public void setLastUse(LocalDate lastUse) {
        this.lastUse = lastUse;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public Tag publications(Set<Publication> publications) {
        this.publications = publications;
        return this;
    }

    public Tag addPublication(Publication publication) {
        this.publications.add(publication);
        publication.getTags().add(this);
        return this;
    }

    public Tag removePublication(Publication publication) {
        this.publications.remove(publication);
        publication.getTags().remove(this);
        return this;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tag tag = (Tag) o;
        if (tag.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tag.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastUse='" + getLastUse() + "'" +
            ", publications='" + getPublications() + "'" +
            "}"
            ;
    }
}
