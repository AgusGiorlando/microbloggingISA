package ar.edu.um.isa.web.rest;

import com.codahale.metrics.annotation.Timed;
import ar.edu.um.isa.domain.Tag;
import ar.edu.um.isa.repository.TagRepository;
import ar.edu.um.isa.web.rest.errors.BadRequestAlertException;
import ar.edu.um.isa.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tag.
 */
@RestController
@RequestMapping("/api")
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    private static final String ENTITY_NAME = "tag";

    private final TagRepository tagRepository;

    public TagResource(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * POST  /tags : Create a new tag.
     *
     * @param tag the tag to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tag, or with status 400 (Bad Request) if the tag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tags")
    @Timed
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) throws URISyntaxException {
        log.debug("REST request to save Tag : {}", tag);
        if (tag.getId() != null) {
            throw new BadRequestAlertException("A new tag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tag result = tagRepository.save(tag);
        return ResponseEntity.created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tags : Updates an existing tag.
     *
     * @param tag the tag to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tag,
     * or with status 400 (Bad Request) if the tag is not valid,
     * or with status 500 (Internal Server Error) if the tag couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tags")
    @Timed
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag) throws URISyntaxException {
        log.debug("REST request to update Tag : {}", tag);
        if (tag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tag result = tagRepository.save(tag);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tag.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tags : get all the tags.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tags in body
     */
    @GetMapping("/tags")
    @Timed
    public List<Tag> getAllTags() {
        log.debug("REST request to get all Tags");
        return tagRepository.findAll();
    }

    /**
     * GET  /tags/:id : get the "id" tag.
     *
     * @param id the id of the tag to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tag, or with status 404 (Not Found)
     */
    @GetMapping("/tags/{id}")
    @Timed
    public ResponseEntity<Tag> getTag(@PathVariable Long id) {
        log.debug("REST request to get Tag : {}", id);
        Optional<Tag> tag = tagRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tag);
    }

    /**
     * DELETE  /tags/:id : delete the "id" tag.
     *
     * @param id the id of the tag to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        log.debug("REST request to delete Tag : {}", id);

        tagRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
//Metodos Agregados
    //Tag populares
@GetMapping("/tag_popular/{dias}")
@Timed
public List<Tag> getTagPopular(@PathVariable int dias) {
    log.debug("REST request to get Publication : {}");
    Calendar fecha = Calendar.getInstance();

    LocalDate fecha_actual = LocalDate.of(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH)+1,fecha.get(Calendar.DAY_OF_MONTH));

    fecha.add(Calendar.DAY_OF_YEAR, -dias);

    LocalDate fecha_vieja=LocalDate.of(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH)+1,fecha.get(Calendar.DAY_OF_MONTH));

    List<Tag> tag = tagRepository.findAllByLastUseIsAfter(fecha_vieja);

    return tag;

}
}
