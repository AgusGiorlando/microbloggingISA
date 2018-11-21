package ar.edu.um.isa.web.rest;

import ar.edu.um.isa.domain.Tag;
import ar.edu.um.isa.domain.User;
import ar.edu.um.isa.repository.PublisherRepository;
import ar.edu.um.isa.repository.TagRepository;
import ar.edu.um.isa.repository.UserRepository;
import com.codahale.metrics.annotation.Timed;
import ar.edu.um.isa.domain.Publication;
import ar.edu.um.isa.repository.PublicationRepository;
import ar.edu.um.isa.web.rest.errors.BadRequestAlertException;
import ar.edu.um.isa.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Publication.
 */
@RestController
@RequestMapping("/api")
public class PublicationResource {

    private final Logger log = LoggerFactory.getLogger(PublicationResource.class);

    private static final String ENTITY_NAME = "publication";

    private final PublicationRepository publicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private TagRepository tagRepository;

    public PublicationResource(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    /**
     * POST  /publications : Create a new publication.
     *
     * @param publication the publication to create
     * @return the ResponseEntity with status 201 (Created) and with body the new publication, or with status 400 (Bad Request) if the publication has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/publications")
    @Timed
    public ResponseEntity<Publication> createPublication(@RequestBody Publication publication) throws URISyntaxException {
        log.debug("REST request to save Publication : {}", publication);
        if (publication.getId() != null) {
            throw new BadRequestAlertException("A new publication cannot already have an ID", ENTITY_NAME, "idexists");
        }

        List <String> mentions  = publication.analizeMentions();
        if (!mentions.isEmpty()) {
            Iterator<String> mentionsIt = mentions.iterator();
            while (mentionsIt.hasNext()) {
                User user = userRepository.findOneByLogin(mentionsIt.next()).orElse(null);
                if (user != null) {
                    log.debug("Mention found : {}", publication);
                    publication.addMention(publisherRepository.findByUser_Id(user.getId()));
                }
            }
        }

        List<String> tags = publication.analizeTags();
        if(!tags.isEmpty()){
            Iterator<String> tagsIt = tags.iterator();
            while (tagsIt.hasNext()){
                String tagName = tagsIt.next();
                Tag tag = tagRepository.findByName(tagName);
                if (tag == null){
                    Tag newtag = new Tag();
                    newtag.setName(tagName);
                    newtag.setLastUse(publication.getDate());
                    tagRepository.save(newtag);
                    log.debug("New tag found: {}", newtag);
                }else {
                    tag.setLastUse(publication.getDate());
                    tagRepository.save(tag);
                    log.debug("Tag updated: {}", tag);
                }
            }
        }

        Publication result = publicationRepository.save(publication);

        return ResponseEntity.created(new URI("/api/publications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /publications : Updates an existing publication.
     *
     * @param publication the publication to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated publication,
     * or with status 400 (Bad Request) if the publication is not valid,
     * or with status 500 (Internal Server Error) if the publication couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/publications")
    @Timed
    public ResponseEntity<Publication> updatePublication(@RequestBody Publication publication) throws URISyntaxException {
        log.debug("REST request to update Publication : {}", publication);
        if (publication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Publication result = publicationRepository.save(publication);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, publication.getId().toString()))
            .body(result);
    }

    /**
     * GET  /publications : get all the publications.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of publications in body
     */
    @GetMapping("/publications")
    @Timed
    public List<Publication> getAllPublications(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Publications");
        return publicationRepository.findAll();
    }

    /**
     * GET  /publications/:id : get the "id" publication.
     *
     * @param id the id of the publication to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the publication, or with status 404 (Not Found)
     */
    @GetMapping("/publications/{id}")
    @Timed
    public ResponseEntity<Publication> getPublication(@PathVariable Long id) {
        log.debug("REST request to get Publication : {}", id);
        Optional<Publication> publication = publicationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(publication);
    }

    /**
     * DELETE  /publications/:id : delete the "id" publication.
     *
     * @param id the id of the publication to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/publications/{id}")
    @Timed
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        log.debug("REST request to delete Publication : {}", id);

        publicationRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //Metodos Agregados
//Ver la publicaciones de un usuario determinado.
    @GetMapping("/publications/publisher/{id}")
    @Timed
    public List<Publication> getPublicationsByPublisher(@PathVariable Long id) {
        log.debug("REST request to get Publication : {}", id);
        Optional<Publisher> publisher = publisherRepository.findById(id);
        List<Publication> publications = publicationRepository.findPublicationsByPublisher(publisher.get());
        return publications;

    }

    //Ver todas las publicaciones de un tag determinado
    @GetMapping("/publications/tag/{id}")
    @Timed
    public List<Publication> getPublicationsByTag(@PathVariable Long id) {
        log.debug("REST request to get Publication : {}", id);
        Optional<Tag> tag = tagRepository.findById(id);
        List<Publication> publications = publicationRepository.findPublicationsByTags(tag.get());
        return publications;

    }


    //Ver todas las publicaciones de una mencion determinada
    @GetMapping("/publications/mentios/{id}")
    @Timed
    public List<Publication> getPublicationsByMentions(@PathVariable Long id) {
        log.debug("REST request to get Publication : {}", id);
        Optional<Publisher> publisher_mentions = publisherRepository.findById(id);
        List<Publication> publications = publicationRepository.findPublicationsByMentions(publisher_mentions.get());
        return publications;

    }
    //Marcar como favorito publications
    @PutMapping("/publications/faved/{id_publications}/{id_publisher}")
    @Timed
    public Publication favedPublication(@PathVariable Long id_publications, @PathVariable Long id_publisher) throws URISyntaxException {
        Optional<Publication> publication = publicationRepository.findById(id_publications);
        Optional<Publisher> publisher = publisherRepository.findById(id_publisher);

        log.debug("REST request to faved Publication : {}", publication);

        if (publicationRepository.findById(id_publications) == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        publication.get().addFavedBy(publisher.get());

        Publication result = publicationRepository.save(publication.get());
        return result;


    }
  //Republicar
  @PutMapping("/publications/republish/{id_publication}/{id_republish}")
  @Timed
  public Publication republishPublication(@PathVariable Long id_publication, @PathVariable Long id_republish) throws URISyntaxException {
      Optional<Publication> publication = publicationRepository.findById(id_publication);
      Optional<Publication> republish = publicationRepository.findById(id_republish);

      log.debug("REST request to faved Publication : {}", publication);

      if (publicationRepository.findById(id_publication) == publicationRepository.findById(id_republish)) {
          throw new BadRequestAlertException("Publicatios is same to republish", ENTITY_NAME, "idincompatible");
      }
      publication.get().republish(republish.get());

      Publication result = publicationRepository.save(publication.get());
      return result;


  }
}



