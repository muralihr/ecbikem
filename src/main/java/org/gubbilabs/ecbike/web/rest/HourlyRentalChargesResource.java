package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.HourlyRentalCharges;
import org.gubbilabs.ecbike.repository.HourlyRentalChargesRepository;
import org.gubbilabs.ecbike.repository.search.HourlyRentalChargesSearchRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing HourlyRentalCharges.
 */
@RestController
@RequestMapping("/api")
public class HourlyRentalChargesResource {

    private final Logger log = LoggerFactory.getLogger(HourlyRentalChargesResource.class);
        
    @Inject
    private HourlyRentalChargesRepository hourlyRentalChargesRepository;
    
    @Inject
    private HourlyRentalChargesSearchRepository hourlyRentalChargesSearchRepository;
    
    /**
     * POST  /hourly-rental-charges : Create a new hourlyRentalCharges.
     *
     * @param hourlyRentalCharges the hourlyRentalCharges to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hourlyRentalCharges, or with status 400 (Bad Request) if the hourlyRentalCharges has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hourly-rental-charges",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HourlyRentalCharges> createHourlyRentalCharges(@Valid @RequestBody HourlyRentalCharges hourlyRentalCharges) throws URISyntaxException {
        log.debug("REST request to save HourlyRentalCharges : {}", hourlyRentalCharges);
        if (hourlyRentalCharges.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hourlyRentalCharges", "idexists", "A new hourlyRentalCharges cannot already have an ID")).body(null);
        }
        HourlyRentalCharges result = hourlyRentalChargesRepository.save(hourlyRentalCharges);
        hourlyRentalChargesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/hourly-rental-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hourlyRentalCharges", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hourly-rental-charges : Updates an existing hourlyRentalCharges.
     *
     * @param hourlyRentalCharges the hourlyRentalCharges to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hourlyRentalCharges,
     * or with status 400 (Bad Request) if the hourlyRentalCharges is not valid,
     * or with status 500 (Internal Server Error) if the hourlyRentalCharges couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/hourly-rental-charges",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HourlyRentalCharges> updateHourlyRentalCharges(@Valid @RequestBody HourlyRentalCharges hourlyRentalCharges) throws URISyntaxException {
        log.debug("REST request to update HourlyRentalCharges : {}", hourlyRentalCharges);
        if (hourlyRentalCharges.getId() == null) {
            return createHourlyRentalCharges(hourlyRentalCharges);
        }
        HourlyRentalCharges result = hourlyRentalChargesRepository.save(hourlyRentalCharges);
        hourlyRentalChargesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hourlyRentalCharges", hourlyRentalCharges.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hourly-rental-charges : get all the hourlyRentalCharges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hourlyRentalCharges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/hourly-rental-charges",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<HourlyRentalCharges>> getAllHourlyRentalCharges(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of HourlyRentalCharges");
        Page<HourlyRentalCharges> page = hourlyRentalChargesRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hourly-rental-charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hourly-rental-charges/:id : get the "id" hourlyRentalCharges.
     *
     * @param id the id of the hourlyRentalCharges to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hourlyRentalCharges, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/hourly-rental-charges/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HourlyRentalCharges> getHourlyRentalCharges(@PathVariable Long id) {
        log.debug("REST request to get HourlyRentalCharges : {}", id);
        HourlyRentalCharges hourlyRentalCharges = hourlyRentalChargesRepository.findOne(id);
        return Optional.ofNullable(hourlyRentalCharges)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hourly-rental-charges/:id : delete the "id" hourlyRentalCharges.
     *
     * @param id the id of the hourlyRentalCharges to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/hourly-rental-charges/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHourlyRentalCharges(@PathVariable Long id) {
        log.debug("REST request to delete HourlyRentalCharges : {}", id);
        hourlyRentalChargesRepository.delete(id);
        hourlyRentalChargesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hourlyRentalCharges", id.toString())).build();
    }

    /**
     * SEARCH  /_search/hourly-rental-charges?query=:query : search for the hourlyRentalCharges corresponding
     * to the query.
     *
     * @param query the query of the hourlyRentalCharges search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/hourly-rental-charges",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<HourlyRentalCharges>> searchHourlyRentalCharges(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of HourlyRentalCharges for query {}", query);
        Page<HourlyRentalCharges> page = hourlyRentalChargesSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/hourly-rental-charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
