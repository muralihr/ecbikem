package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.CycleToRentalNodeMapper;
import org.gubbilabs.ecbike.repository.CycleToRentalNodeMapperRepository;
import org.gubbilabs.ecbike.repository.search.CycleToRentalNodeMapperSearchRepository;
import org.gubbilabs.ecbike.web.rest.util.HeaderUtil;
import org.gubbilabs.ecbike.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CycleToRentalNodeMapper.
 */
@RestController
@RequestMapping("/api")
public class CycleToRentalNodeMapperResource {

    private final Logger log = LoggerFactory.getLogger(CycleToRentalNodeMapperResource.class);
        
    @Inject
    private CycleToRentalNodeMapperRepository cycleToRentalNodeMapperRepository;
    
    @Inject
    private CycleToRentalNodeMapperSearchRepository cycleToRentalNodeMapperSearchRepository;
    
    /**
     * POST  /cycle-to-rental-node-mappers : Create a new cycleToRentalNodeMapper.
     *
     * @param cycleToRentalNodeMapper the cycleToRentalNodeMapper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cycleToRentalNodeMapper, or with status 400 (Bad Request) if the cycleToRentalNodeMapper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycle-to-rental-node-mappers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToRentalNodeMapper> createCycleToRentalNodeMapper(@RequestBody CycleToRentalNodeMapper cycleToRentalNodeMapper) throws URISyntaxException {
        log.debug("REST request to save CycleToRentalNodeMapper : {}", cycleToRentalNodeMapper);
        if (cycleToRentalNodeMapper.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cycleToRentalNodeMapper", "idexists", "A new cycleToRentalNodeMapper cannot already have an ID")).body(null);
        }
        CycleToRentalNodeMapper result = cycleToRentalNodeMapperRepository.save(cycleToRentalNodeMapper);
        cycleToRentalNodeMapperSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cycle-to-rental-node-mappers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cycleToRentalNodeMapper", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cycle-to-rental-node-mappers : Updates an existing cycleToRentalNodeMapper.
     *
     * @param cycleToRentalNodeMapper the cycleToRentalNodeMapper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cycleToRentalNodeMapper,
     * or with status 400 (Bad Request) if the cycleToRentalNodeMapper is not valid,
     * or with status 500 (Internal Server Error) if the cycleToRentalNodeMapper couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycle-to-rental-node-mappers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToRentalNodeMapper> updateCycleToRentalNodeMapper(@RequestBody CycleToRentalNodeMapper cycleToRentalNodeMapper) throws URISyntaxException {
        log.debug("REST request to update CycleToRentalNodeMapper : {}", cycleToRentalNodeMapper);
        if (cycleToRentalNodeMapper.getId() == null) {
            return createCycleToRentalNodeMapper(cycleToRentalNodeMapper);
        }
        CycleToRentalNodeMapper result = cycleToRentalNodeMapperRepository.save(cycleToRentalNodeMapper);
        cycleToRentalNodeMapperSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cycleToRentalNodeMapper", cycleToRentalNodeMapper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cycle-to-rental-node-mappers : get all the cycleToRentalNodeMappers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cycleToRentalNodeMappers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cycle-to-rental-node-mappers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CycleToRentalNodeMapper>> getAllCycleToRentalNodeMappers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CycleToRentalNodeMappers");
        Page<CycleToRentalNodeMapper> page = cycleToRentalNodeMapperRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cycle-to-rental-node-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cycle-to-rental-node-mappers/:id : get the "id" cycleToRentalNodeMapper.
     *
     * @param id the id of the cycleToRentalNodeMapper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cycleToRentalNodeMapper, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cycle-to-rental-node-mappers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToRentalNodeMapper> getCycleToRentalNodeMapper(@PathVariable Long id) {
        log.debug("REST request to get CycleToRentalNodeMapper : {}", id);
        CycleToRentalNodeMapper cycleToRentalNodeMapper = cycleToRentalNodeMapperRepository.findOne(id);
        return Optional.ofNullable(cycleToRentalNodeMapper)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cycle-to-rental-node-mappers/:id : delete the "id" cycleToRentalNodeMapper.
     *
     * @param id the id of the cycleToRentalNodeMapper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cycle-to-rental-node-mappers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCycleToRentalNodeMapper(@PathVariable Long id) {
        log.debug("REST request to delete CycleToRentalNodeMapper : {}", id);
        cycleToRentalNodeMapperRepository.delete(id);
        cycleToRentalNodeMapperSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cycleToRentalNodeMapper", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cycle-to-rental-node-mappers?query=:query : search for the cycleToRentalNodeMapper corresponding
     * to the query.
     *
     * @param query the query of the cycleToRentalNodeMapper search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cycle-to-rental-node-mappers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CycleToRentalNodeMapper>> searchCycleToRentalNodeMappers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CycleToRentalNodeMappers for query {}", query);
        Page<CycleToRentalNodeMapper> page = cycleToRentalNodeMapperSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cycle-to-rental-node-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
