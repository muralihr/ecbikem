package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.CycleToCustomerMapper;
import org.gubbilabs.ecbike.repository.CycleToCustomerMapperRepository;
import org.gubbilabs.ecbike.repository.search.CycleToCustomerMapperSearchRepository;
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
 * REST controller for managing CycleToCustomerMapper.
 */
@RestController
@RequestMapping("/api")
public class CycleToCustomerMapperResource {

    private final Logger log = LoggerFactory.getLogger(CycleToCustomerMapperResource.class);
        
    @Inject
    private CycleToCustomerMapperRepository cycleToCustomerMapperRepository;
    
    @Inject
    private CycleToCustomerMapperSearchRepository cycleToCustomerMapperSearchRepository;
    
    /**
     * POST  /cycle-to-customer-mappers : Create a new cycleToCustomerMapper.
     *
     * @param cycleToCustomerMapper the cycleToCustomerMapper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cycleToCustomerMapper, or with status 400 (Bad Request) if the cycleToCustomerMapper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycle-to-customer-mappers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToCustomerMapper> createCycleToCustomerMapper(@RequestBody CycleToCustomerMapper cycleToCustomerMapper) throws URISyntaxException {
        log.debug("REST request to save CycleToCustomerMapper : {}", cycleToCustomerMapper);
        if (cycleToCustomerMapper.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cycleToCustomerMapper", "idexists", "A new cycleToCustomerMapper cannot already have an ID")).body(null);
        }
        CycleToCustomerMapper result = cycleToCustomerMapperRepository.save(cycleToCustomerMapper);
        cycleToCustomerMapperSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cycle-to-customer-mappers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cycleToCustomerMapper", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cycle-to-customer-mappers : Updates an existing cycleToCustomerMapper.
     *
     * @param cycleToCustomerMapper the cycleToCustomerMapper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cycleToCustomerMapper,
     * or with status 400 (Bad Request) if the cycleToCustomerMapper is not valid,
     * or with status 500 (Internal Server Error) if the cycleToCustomerMapper couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycle-to-customer-mappers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToCustomerMapper> updateCycleToCustomerMapper(@RequestBody CycleToCustomerMapper cycleToCustomerMapper) throws URISyntaxException {
        log.debug("REST request to update CycleToCustomerMapper : {}", cycleToCustomerMapper);
        if (cycleToCustomerMapper.getId() == null) {
            return createCycleToCustomerMapper(cycleToCustomerMapper);
        }
        CycleToCustomerMapper result = cycleToCustomerMapperRepository.save(cycleToCustomerMapper);
        cycleToCustomerMapperSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cycleToCustomerMapper", cycleToCustomerMapper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cycle-to-customer-mappers : get all the cycleToCustomerMappers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cycleToCustomerMappers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cycle-to-customer-mappers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CycleToCustomerMapper>> getAllCycleToCustomerMappers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CycleToCustomerMappers");
        Page<CycleToCustomerMapper> page = cycleToCustomerMapperRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cycle-to-customer-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cycle-to-customer-mappers/:id : get the "id" cycleToCustomerMapper.
     *
     * @param id the id of the cycleToCustomerMapper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cycleToCustomerMapper, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cycle-to-customer-mappers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToCustomerMapper> getCycleToCustomerMapper(@PathVariable Long id) {
        log.debug("REST request to get CycleToCustomerMapper : {}", id);
        CycleToCustomerMapper cycleToCustomerMapper = cycleToCustomerMapperRepository.findOne(id);
        return Optional.ofNullable(cycleToCustomerMapper)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cycle-to-customer-mappers/:id : delete the "id" cycleToCustomerMapper.
     *
     * @param id the id of the cycleToCustomerMapper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cycle-to-customer-mappers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCycleToCustomerMapper(@PathVariable Long id) {
        log.debug("REST request to delete CycleToCustomerMapper : {}", id);
        cycleToCustomerMapperRepository.delete(id);
        cycleToCustomerMapperSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cycleToCustomerMapper", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cycle-to-customer-mappers?query=:query : search for the cycleToCustomerMapper corresponding
     * to the query.
     *
     * @param query the query of the cycleToCustomerMapper search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cycle-to-customer-mappers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CycleToCustomerMapper>> searchCycleToCustomerMappers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CycleToCustomerMappers for query {}", query);
        Page<CycleToCustomerMapper> page = cycleToCustomerMapperSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cycle-to-customer-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
