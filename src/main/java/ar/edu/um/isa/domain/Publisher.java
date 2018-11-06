package ar.edu.um.isa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Publisher.
 */
@Entity
@Table(name = "publisher")
public class Publisher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "publisher")
    private Set<Publication> publications = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "publisher_follow",
               joinColumns = @JoinColumn(name = "publishers_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "follows_id", referencedColumnName = "id"))
    private Set<Publisher> follows = new HashSet<>();

    @ManyToMany(mappedBy = "favedBies")
    @JsonIgnore
    private Set<Publication> favourites = new HashSet<>();

    @ManyToMany(mappedBy = "follows")
    @JsonIgnore
    private Set<Publisher> followers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Publisher user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Publication> getPublications() {
        return publications;
    }

    public Publisher publications(Set<Publication> publications) {
        this.publications = publications;
        return this;
    }

    public Publisher addPublication(Publication publication) {
        this.publications.add(publication);
        publication.setPublisher(this);
        return this;
    }

    public Publisher removePublication(Publication publication) {
        this.publications.remove(publication);
        publication.setPublisher(null);
        return this;
    }

    public void setPublications(Set<Publication> publications) {
        this.publications = publications;
    }

    public Set<Publisher> getFollows() {
        return follows;
    }

    public Publisher follows(Set<Publisher> publishers) {
        this.follows = publishers;
        return this;
    }

    public Publisher addFollow(Publisher publisher) {
        this.follows.add(publisher);
        publisher.getFollowers().add(this);
        return this;
    }

    public Publisher removeFollow(Publisher publisher) {
        this.follows.remove(publisher);
        publisher.getFollowers().remove(this);
        return this;
    }

    public void setFollows(Set<Publisher> publishers) {
        this.follows = publishers;
    }

    public Set<Publication> getFavourites() {
        return favourites;
    }

    public Publisher favourites(Set<Publication> publications) {
        this.favourites = publications;
        return this;
    }

    public Publisher addFavourite(Publication publication) {
        this.favourites.add(publication);
        publication.getFavedBies().add(this);
        return this;
    }

    public Publisher removeFavourite(Publication publication) {
        this.favourites.remove(publication);
        publication.getFavedBies().remove(this);
        return this;
    }

    public void setFavourites(Set<Publication> publications) {
        this.favourites = publications;
    }

    public Set<Publisher> getFollowers() {
        return followers;
    }

    public Publisher followers(Set<Publisher> publishers) {
        this.followers = publishers;
        return this;
    }

    public Publisher addFollower(Publisher publisher) {
        this.followers.add(publisher);
        publisher.getFollows().add(this);
        return this;
    }

    public Publisher removeFollower(Publisher publisher) {
        this.followers.remove(publisher);
        publisher.getFollows().remove(this);
        return this;
    }

    public void setFollowers(Set<Publisher> publishers) {
        this.followers = publishers;
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
        Publisher publisher = (Publisher) o;
        if (publisher.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), publisher.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Publisher{" +
            "id=" + getId() +
            "}";
    }
}
