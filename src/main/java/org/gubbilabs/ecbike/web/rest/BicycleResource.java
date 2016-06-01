package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.Bicycle;
import org.gubbilabs.ecbike.domain.CycleToRentalNodeMapper;
import org.gubbilabs.ecbike.domain.CycleToStockNodeMapper;
import org.gubbilabs.ecbike.repository.BicycleRepository;
import org.gubbilabs.ecbike.repository.CycleToRentalNodeMapperRepository;
import org.gubbilabs.ecbike.repository.CycleToStockNodeMapperRepository;
import org.gubbilabs.ecbike.repository.search.BicycleSearchRepository;
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
 * REST controller for managing Bicycle.
 */
@RestController
@RequestMapping("/api")
public class BicycleResource {

    private final Logger log = LoggerFactory.getLogger(BicycleResource.class);
        
    @Inject
    private BicycleRepository bicycleRepository;
    
    @Inject
    private BicycleSearchRepository bicycleSearchRepository;
    

    
    @Inject
    private CycleToStockNodeMapperRepository cycleToStockNodeMapperRepository;

    @Inject
	private CycleToRentalNodeMapperRepository cycleToRentalNodeMapperRepository;

    
    /**
     * POST  /bicycles : Create a new bicycle.
     *
     * @param bicycle the bicycle to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bicycle, or with status 400 (Bad Request) if the bicycle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bicycles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bicycle> createBicycle(@Valid @RequestBody Bicycle bicycle) throws URISyntaxException {
        log.debug("REST request to save Bicycle : {}", bicycle);
        if (bicycle.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bicycle", "idexists", "A new bicycle cannot already have an ID")).body(null);
        }
        Bicycle result = bicycleRepository.save(bicycle);
        bicycleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bicycles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bicycle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bicycles : Updates an existing bicycle.
     *
     * @param bicycle the bicycle to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bicycle,
     * or with status 400 (Bad Request) if the bicycle is not valid,
     * or with status 500 (Internal Server Error) if the bicycle couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bicycles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bicycle> updateBicycle(@Valid @RequestBody Bicycle bicycle) throws URISyntaxException {
        log.debug("REST request to update Bicycle : {}", bicycle);
        if (bicycle.getId() == null) {
            return createBicycle(bicycle);
        }
        Bicycle result = bicycleRepository.save(bicycle);
        bicycleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bicycle", bicycle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bicycles : get all the bicycles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bicycles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bicycles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bicycle>> getAllBicycles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Bicycles");
        Page<Bicycle> page = bicycleRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bicycles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bicycles/:id : get the "id" bicycle.
     *
     * @param id the id of the bicycle to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bicycle, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bicycles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Bicycle> getBicycle(@PathVariable Long id) {
        log.debug("REST request to get Bicycle : {}", id);
        Bicycle bicycle = bicycleRepository.findOne(id);
        return Optional.ofNullable(bicycle)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bicycles/:id : delete the "id" bicycle.
     *
     * @param id the id of the bicycle to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bicycles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBicycle(@PathVariable Long id) {
        log.debug("REST request to delete Bicycle : {}", id);
        bicycleRepository.delete(id);
        bicycleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bicycle", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bicycles?query=:query : search for the bicycle corresponding
     * to the query.
     *
     * @param query the query of the bicycle search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/bicycles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bicycle>> searchBicycles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Bicycles for query {}", query);
        Page<Bicycle> page = bicycleSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bicycles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/bicycles/untaggedbicycles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bicycle>> getAllUntaggedCycles( )
        throws URISyntaxException {
        List<Bicycle> listOfCycles = bicycleRepository.findAllUnassignedCycles( );
        HttpHeaders headers = new HttpHeaders();// PaginationUtil.generatePaginationHttpHeaders(page, "/api/bicycles", offset, limit);
        return new ResponseEntity<>(listOfCycles, headers, HttpStatus.OK);
    }
    

    @RequestMapping(value = "/bicycles/bicyclesatstock/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bicycle>> getAllCyclesAtStock( @PathVariable Long id)
        throws URISyntaxException {
    	
    	List<CycleToStockNodeMapper> cycleToStockNodeMapper = cycleToStockNodeMapperRepository.findAllCyclesAtStock(id);
        List<Bicycle> listOfCycles = new LinkedList<Bicycle>();
    	for(CycleToStockNodeMapper t : cycleToStockNodeMapper)
    	{
    		listOfCycles.add(t.getMovedCycle());
    	}
        HttpHeaders headers = new HttpHeaders();// PaginationUtil.generatePaginationHttpHeaders(page, "/api/bicycles", offset, limit);
        return new ResponseEntity<>(listOfCycles, headers, HttpStatus.OK);
    }
    

    @RequestMapping(value = "/bicycles/bicyclesatnode/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Bicycle>> getAllCyclesAtNode(@PathVariable Long id )
        throws URISyntaxException {
    	List<CycleToRentalNodeMapper> cycleToRentalNodeMapper = cycleToRentalNodeMapperRepository.findAllCyclesAtNode(id);
        List<Bicycle> listOfCycles = new LinkedList<Bicycle>();
    	for(CycleToRentalNodeMapper t : cycleToRentalNodeMapper)
    	{
    		listOfCycles.add(t.getMovedCycle());
    	}
        HttpHeaders headers = new HttpHeaders();// PaginationUtil.generatePaginationHttpHeaders(page, "/api/bicycles", offset, limit);
        return new ResponseEntity<>(listOfCycles, headers, HttpStatus.OK);
    }

}
