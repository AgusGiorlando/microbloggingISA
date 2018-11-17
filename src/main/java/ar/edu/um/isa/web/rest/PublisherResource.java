package ar.edu.um.isa.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.isa.domain.Publisher;
import ar.edu.um.isa.repository.PublisherRepository;
import ar.edu.um.isa.web.rest.errors.BadRequestAlertException;
import ar.edu.um.isa.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Publisher.
 */
@RestController
@RequestMapping("/api")
public class PublisherResource {

    private final Logger log = LoggerFactory.getLogger(PublisherResource.class);

    private static final String ENTITY_NAME = "publisher";

    private final PublisherRepository publisherRepository;

    public PublisherResource(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * POST  /publishers : Create a new publisher.
     *
     * @param publisher the publisher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publisher, or with status 400 (Bad Request) if the publisher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/publishers")
    @Timed
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) throws URISyntaxException {
        log.debug("REST request to save Publisher : {}", publisher);
        if (publisher.getId() != null) {
            throw new BadRequestAlertException("A new publisher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity.created(new URI("/api/publishers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publishers : Updates an existing publisher.
     *
     * @param publisher the publisher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publisher,
     * or with status 400 (Bad Request) if the publisher is not valid,
     * or with status 500 (Internal Server Error) if the publisher couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/publishers")
    @Timed
    public ResponseEntity<Publisher> updatePublisher(@RequestBody Publisher publisher) throws URISyntaxException {
        log.debug("REST request to update Publisher : {}", publisher);
        if (publisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, publisher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /publishers : get all the publishers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of publishers in body
     */
    @GetMapping("/publishers")
    @Timed
    public List<Publisher> getAllPublishers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Publishers");
        return publisherRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /publishers/:id : get the "id" publisher.
     *
     * @param id the id of the publisher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publisher, or with status 404 (Not Found)
     */
    @GetMapping("/publishers/{id}")
    @Timed
    public ResponseEntity<Publisher> getPublisher(@PathVariable Long id) {
        log.debug("REST request to get Publisher : {}", id);
        Optional<Publisher> publisher = publisherRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(publisher);
    }

    /**
     * DELETE  /publishers/:id : delete the "id" publisher.
     *
     * @param id the id of the publisher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/publishers/{id}")
    @Timed
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        log.debug("REST request to delete Publisher : {}", id);

        publisherRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * PUT  /publishers/follow/:followed_id/:follower_id : Follows an existing publisher.
     * @param follower_id publisher who follows
     * @param followed_id publisher followed
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/publishers/follow/{followed_id}/{follower_id}")
    @Timed
    public ResponseEntity<Publisher> followPublisher(@PathVariable Long follower_id, @PathVariable Long followed_id) throws URISyntaxException {
        log.debug("REST request to follow a Publisher : {}", followed_id);

        Optional<Publisher> followed = publisherRepository.findById(followed_id);
        Optional<Publisher> follower = publisherRepository.findById(follower_id);

        if (publisherRepository.findById(followed_id) == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        else if (followed_id.equals(follower_id)){
            throw new UnsupportedOperationException("Cannot follow himself");
        }
        else if (follower.get().getFollows().contains(followed)){
            throw new UnsupportedOperationException("Already followed");
        }

        follower.get().addFollow(followed.get());
        followed.get().addFollower(follower.get());

        Publisher result = publisherRepository.save(follower.get());
        publisherRepository.save(followed.get());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, follower.get().getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publishers/unfollow/:followed_id/:follower_id : Unfollows a followed publisher.
     * @param follower_id publisher who follows
     * @param followed_id publisher followed
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/publishers/unfollow/{followed_id}/{follower_id}")
    @Timed
    public ResponseEntity<Publisher> unFollowPublisher(@PathVariable Long follower_id, @PathVariable Long followed_id) throws URISyntaxException {
        log.debug("REST request to unfollow a Publisher : {}", followed_id);

        Optional<Publisher> followed = publisherRepository.findById(followed_id);
        Optional<Publisher> follower = publisherRepository.findById(follower_id);

        if (followed_id.equals(follower_id)){
            throw new UnsupportedOperationException("Cannot unfollow himself");
        }
        else if (!follower.get().getFollows().contains(followed)){
            throw new UnsupportedOperationException("Already followed");
        }

        follower.get().removeFollow(followed.get());
        followed.get().removeFollower(follower.get());

        Publisher result = publisherRepository.save(follower.get());
        publisherRepository.save(followed.get());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, follower.get().getId().toString()))
            .body(result);
    }

    /**
     * GET  /publishers/:id/followers : get the followers list of a publisher.
     *
     * @param id the id of the publisher
     * @return the ResponseEntity with status 200 (OK) and with body the list of followers, or with status 404 (Not Found)
     */
    @GetMapping("/publishers/{id}/followers")
    @Timed
    public ResponseEntity<Set<Publisher>> getFollowers(@PathVariable Long id) {
        log.debug("REST request to get followers : {}", id);

        if (publisherRepository.findById(id) == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Publisher> publisher = publisherRepository.findById(id);

        Set<Publisher> followers = publisher.get().getFollowers();

        return ResponseEntity.ok(followers);
    }

    /**
     * GET  /publishers/:id/follows : get the follows list of a publisher.
     *
     * @param id the id of the publisher
     * @return the ResponseEntity with status 200 (OK) and with body the list of publishers followed, or with status 404 (Not Found)
     */
    @GetMapping("/publishers/{id}/follows")
    @Timed
    public ResponseEntity<Set<Publisher>> getFollows(@PathVariable Long id) {
        log.debug("REST request to get follows : {}", id);

        if (publisherRepository.findById(id) == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Publisher> publisher = publisherRepository.findById(id);

        Set<Publisher> follows = publisher.get().getFollowers();

        return ResponseEntity.ok(follows);
    }
}
