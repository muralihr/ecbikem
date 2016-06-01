package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.CycleToStockNodeMapper;
import org.gubbilabs.ecbike.domain.util.MoveCycleResponseJsonObject;
import org.gubbilabs.ecbike.domain.util.MoveStockToRentalNodeMapper;
import org.gubbilabs.ecbike.repository.CycleToStockNodeMapperRepository;
import org.gubbilabs.ecbike.repository.search.CycleToStockNodeMapperSearchRepository;
import org.gubbilabs.ecbike.service.CycleMovementService;
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
 * REST controller for managing CycleToStockNodeMapper.
 */
@RestController
@RequestMapping("/api")
public class CycleToStockNodeMapperResource {

    private final Logger log = LoggerFactory.getLogger(CycleToStockNodeMapperResource.class);
        
    @Inject
    private CycleToStockNodeMapperRepository cycleToStockNodeMapperRepository;
    
    @Inject
    private CycleToStockNodeMapperSearchRepository cycleToStockNodeMapperSearchRepository;

    @Inject
    private CycleMovementService cycleMovementService;
    
    /**
     * POST  /cycle-to-stock-node-mappers : Create a new cycleToStockNodeMapper.
     *
     * @param cycleToStockNodeMapper the cycleToStockNodeMapper to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cycleToStockNodeMapper, or with status 400 (Bad Request) if the cycleToStockNodeMapper has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycle-to-stock-node-mappers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToStockNodeMapper> createCycleToStockNodeMapper(@RequestBody CycleToStockNodeMapper cycleToStockNodeMapper) throws URISyntaxException {
        log.debug("REST request to save CycleToStockNodeMapper : {}", cycleToStockNodeMapper);
        if (cycleToStockNodeMapper.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cycleToStockNodeMapper", "idexists", "A new cycleToStockNodeMapper cannot already have an ID")).body(null);
        }
        CycleToStockNodeMapper result = cycleToStockNodeMapperRepository.save(cycleToStockNodeMapper);
        cycleToStockNodeMapperSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cycle-to-stock-node-mappers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cycleToStockNodeMapper", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cycle-to-stock-node-mappers : Updates an existing cycleToStockNodeMapper.
     *
     * @param cycleToStockNodeMapper the cycleToStockNodeMapper to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cycleToStockNodeMapper,
     * or with status 400 (Bad Request) if the cycleToStockNodeMapper is not valid,
     * or with status 500 (Internal Server Error) if the cycleToStockNodeMapper couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cycle-to-stock-node-mappers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToStockNodeMapper> updateCycleToStockNodeMapper(@RequestBody CycleToStockNodeMapper cycleToStockNodeMapper) throws URISyntaxException {
        log.debug("REST request to update CycleToStockNodeMapper : {}", cycleToStockNodeMapper);
        if (cycleToStockNodeMapper.getId() == null) {
            return createCycleToStockNodeMapper(cycleToStockNodeMapper);
        }
        CycleToStockNodeMapper result = cycleToStockNodeMapperRepository.save(cycleToStockNodeMapper);
        cycleToStockNodeMapperSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cycleToStockNodeMapper", cycleToStockNodeMapper.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cycle-to-stock-node-mappers : get all the cycleToStockNodeMappers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cycleToStockNodeMappers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cycle-to-stock-node-mappers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CycleToStockNodeMapper>> getAllCycleToStockNodeMappers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CycleToStockNodeMappers");
        Page<CycleToStockNodeMapper> page = cycleToStockNodeMapperRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cycle-to-stock-node-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cycle-to-stock-node-mappers/:id : get the "id" cycleToStockNodeMapper.
     *
     * @param id the id of the cycleToStockNodeMapper to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cycleToStockNodeMapper, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cycle-to-stock-node-mappers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CycleToStockNodeMapper> getCycleToStockNodeMapper(@PathVariable Long id) {
        log.debug("REST request to get CycleToStockNodeMapper : {}", id);
        CycleToStockNodeMapper cycleToStockNodeMapper = cycleToStockNodeMapperRepository.findOne(id);
        return Optional.ofNullable(cycleToStockNodeMapper)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cycle-to-stock-node-mappers/:id : delete the "id" cycleToStockNodeMapper.
     *
     * @param id the id of the cycleToStockNodeMapper to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cycle-to-stock-node-mappers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCycleToStockNodeMapper(@PathVariable Long id) {
        log.debug("REST request to delete CycleToStockNodeMapper : {}", id);
        cycleToStockNodeMapperRepository.delete(id);
        cycleToStockNodeMapperSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cycleToStockNodeMapper", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cycle-to-stock-node-mappers?query=:query : search for the cycleToStockNodeMapper corresponding
     * to the query.
     *
     * @param query the query of the cycleToStockNodeMapper search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/cycle-to-stock-node-mappers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CycleToStockNodeMapper>> searchCycleToStockNodeMappers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of CycleToStockNodeMappers for query {}", query);
        Page<CycleToStockNodeMapper> page = cycleToStockNodeMapperSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cycle-to-stock-node-mappers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    //////
    @RequestMapping(value = "/cycle-to-stock-node-mappers/moveCycleToNode",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    
    public ResponseEntity<MoveCycleResponseJsonObject> moveCycleToNode(@RequestBody MoveStockToRentalNodeMapper m) {
 	   
 	   MoveCycleResponseJsonObject responseObject = cycleMovementService.moveCycleToNode(m);
 	   ResponseEntity<MoveCycleResponseJsonObject> t  =   ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("moveCycleToNode",  responseObject.getStatus()+ responseObject.getMessage())).body(responseObject);
 	   return t;
         
    }
    
}
