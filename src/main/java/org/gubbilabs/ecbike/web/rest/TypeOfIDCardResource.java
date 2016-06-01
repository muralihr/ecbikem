package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.TypeOfIDCard;
import org.gubbilabs.ecbike.service.TypeOfIDCardService;
import org.gubbilabs.ecbike.web.rest.util.HeaderUtil;
import org.gubbilabs.ecbike.web.rest.util.PaginationUtil;
import org.gubbilabs.ecbike.web.rest.dto.TypeOfIDCardDTO;
import org.gubbilabs.ecbike.web.rest.mapper.TypeOfIDCardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TypeOfIDCard.
 */
@RestController
@RequestMapping("/api")
public class TypeOfIDCardResource {

    private final Logger log = LoggerFactory.getLogger(TypeOfIDCardResource.class);
        
    @Inject
    private TypeOfIDCardService typeOfIDCardService;
    
    @Inject
    private TypeOfIDCardMapper typeOfIDCardMapper;
    
    /**
     * POST  /type-of-id-cards : Create a new typeOfIDCard.
     *
     * @param typeOfIDCardDTO the typeOfIDCardDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeOfIDCardDTO, or with status 400 (Bad Request) if the typeOfIDCard has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-of-id-cards",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeOfIDCardDTO> createTypeOfIDCard(@Valid @RequestBody TypeOfIDCardDTO typeOfIDCardDTO) throws URISyntaxException {
        log.debug("REST request to save TypeOfIDCard : {}", typeOfIDCardDTO);
        if (typeOfIDCardDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeOfIDCard", "idexists", "A new typeOfIDCard cannot already have an ID")).body(null);
        }
        TypeOfIDCardDTO result = typeOfIDCardService.save(typeOfIDCardDTO);
        return ResponseEntity.created(new URI("/api/type-of-id-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeOfIDCard", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-of-id-cards : Updates an existing typeOfIDCard.
     *
     * @param typeOfIDCardDTO the typeOfIDCardDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeOfIDCardDTO,
     * or with status 400 (Bad Request) if the typeOfIDCardDTO is not valid,
     * or with status 500 (Internal Server Error) if the typeOfIDCardDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-of-id-cards",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeOfIDCardDTO> updateTypeOfIDCard(@Valid @RequestBody TypeOfIDCardDTO typeOfIDCardDTO) throws URISyntaxException {
        log.debug("REST request to update TypeOfIDCard : {}", typeOfIDCardDTO);
        if (typeOfIDCardDTO.getId() == null) {
            return createTypeOfIDCard(typeOfIDCardDTO);
        }
        TypeOfIDCardDTO result = typeOfIDCardService.save(typeOfIDCardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeOfIDCard", typeOfIDCardDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-of-id-cards : get all the typeOfIDCards.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of typeOfIDCards in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/type-of-id-cards",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TypeOfIDCardDTO>> getAllTypeOfIDCards(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TypeOfIDCards");
        Page<TypeOfIDCard> page = typeOfIDCardService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/type-of-id-cards");
        return new ResponseEntity<>(typeOfIDCardMapper.typeOfIDCardsToTypeOfIDCardDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /type-of-id-cards/:id : get the "id" typeOfIDCard.
     *
     * @param id the id of the typeOfIDCardDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeOfIDCardDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-of-id-cards/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeOfIDCardDTO> getTypeOfIDCard(@PathVariable Long id) {
        log.debug("REST request to get TypeOfIDCard : {}", id);
        TypeOfIDCardDTO typeOfIDCardDTO = typeOfIDCardService.findOne(id);
        return Optional.ofNullable(typeOfIDCardDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-of-id-cards/:id : delete the "id" typeOfIDCard.
     *
     * @param id the id of the typeOfIDCardDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-of-id-cards/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeOfIDCard(@PathVariable Long id) {
        log.debug("REST request to delete TypeOfIDCard : {}", id);
        typeOfIDCardService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeOfIDCard", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-of-id-cards?query=:query : search for the typeOfIDCard corresponding
     * to the query.
     *
     * @param query the query of the typeOfIDCard search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-of-id-cards",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TypeOfIDCardDTO>> searchTypeOfIDCards(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of TypeOfIDCards for query {}", query);
        Page<TypeOfIDCard> page = typeOfIDCardService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/type-of-id-cards");
        return new ResponseEntity<>(typeOfIDCardMapper.typeOfIDCardsToTypeOfIDCardDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
