package ar.edu.um.isa.repository;

import ar.edu.um.isa.domain.*;
import ar.edu.um.isa.web.rest.PublicationResource;
import ar.edu.um.isa.web.rest.PublisherResource;
import org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Publication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query(value = "select distinct publication from Publication publication left join fetch publication.mentions left join fetch publication.favedBies left join fetch publication.likedBies left join fetch publication.tags",
        countQuery = "select count(distinct publication) from Publication publication")
    Page<Publication> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct publication from Publication publication left join fetch publication.mentions left join fetch publication.favedBies left join fetch publication.likedBies left join fetch publication.tags")
    List<Publication> findAllWithEagerRelationships();

    @Query("select publication from Publication publication left join fetch publication.mentions left join fetch publication.favedBies left join fetch publication.likedBies left join fetch publication.tags where publication.id =:id")
    Optional<Publication> findOneWithEagerRelationships(@Param("id") Long id);

    List<Publication> findPublicationsByPublisher(Publisher publisher);

    List<Publication> findPublicationsByTags(Tag tag);


    List<Publication> findPublicationsByMentions(Publisher publisher);


}
