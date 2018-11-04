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
    private User is;

    @OneToMany(mappedBy = "publisher")
    private Set<Publication> publications = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "publisher_mentioned_by",
               joinColumns = @JoinColumn(name = "publishers_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "mentioned_bies_id", referencedColumnName = "id"))
    private Set<Publication> mentionedBies = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "publisher_followed_by",
               joinColumns = @JoinColumn(name = "publishers_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "followed_bies_id", referencedColumnName = "id"))
    private Set<Publisher> followedBies = new HashSet<>();

    @ManyToMany(mappedBy = "favedBies")
    @JsonIgnore
    private Set<Publication> favs = new HashSet<>();

    @ManyToMany(mappedBy = "likedBies")
    @JsonIgnore
    private Set<Publication> likes = new HashSet<>();

    @ManyToMany(mappedBy = "followedBies")
    @JsonIgnore
    private Set<Publisher> follows = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getIs() {
        return is;
    }

    public Publisher is(User user) {
        this.is = user;
        return this;
    }

    public void setIs(User user) {
        this.is = user;
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

    public Set<Publication> getMentionedBies() {
        return mentionedBies;
    }

    public Publisher mentionedBies(Set<Publication> publications) {
        this.mentionedBies = publications;
        return this;
    }

    public Publisher addMentionedBy(Publication publication) {
        this.mentionedBies.add(publication);
        publication.getMentions().add(this);
        return this;
    }

    public Publisher removeMentionedBy(Publication publication) {
        this.mentionedBies.remove(publication);
        publication.getMentions().remove(this);
        return this;
    }

    public void setMentionedBies(Set<Publication> publications) {
        this.mentionedBies = publications;
    }

    public Set<Publisher> getFollowedBies() {
        return followedBies;
    }

    public Publisher followedBies(Set<Publisher> publishers) {
        this.followedBies = publishers;
        return this;
    }

    public Publisher addFollowedBy(Publisher publisher) {
        this.followedBies.add(publisher);
        publisher.getFollows().add(this);
        return this;
    }

    public Publisher removeFollowedBy(Publisher publisher) {
        this.followedBies.remove(publisher);
        publisher.getFollows().remove(this);
        return this;
    }

    public void setFollowedBies(Set<Publisher> publishers) {
        this.followedBies = publishers;
    }

    public Set<Publication> getFavs() {
        return favs;
    }

    public Publisher favs(Set<Publication> publications) {
        this.favs = publications;
        return this;
    }

    public Publisher addFav(Publication publication) {
        this.favs.add(publication);
        publication.getFavedBies().add(this);
        return this;
    }

    public Publisher removeFav(Publication publication) {
        this.favs.remove(publication);
        publication.getFavedBies().remove(this);
        return this;
    }

    public void setFavs(Set<Publication> publications) {
        this.favs = publications;
    }

    public Set<Publication> getLikes() {
        return likes;
    }

    public Publisher likes(Set<Publication> publications) {
        this.likes = publications;
        return this;
    }

    public Publisher addLike(Publication publication) {
        this.likes.add(publication);
        publication.getLikedBies().add(this);
        return this;
    }

    public Publisher removeLike(Publication publication) {
        this.likes.remove(publication);
        publication.getLikedBies().remove(this);
        return this;
    }

    public void setLikes(Set<Publication> publications) {
        this.likes = publications;
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
        publisher.getFollowedBies().add(this);
        return this;
    }

    public Publisher removeFollow(Publisher publisher) {
        this.follows.remove(publisher);
        publisher.getFollowedBies().remove(this);
        return this;
    }

    public void setFollows(Set<Publisher> publishers) {
        this.follows = publishers;
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
