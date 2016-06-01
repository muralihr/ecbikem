package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.TripRentUnitsToChargeMap;
import org.gubbilabs.ecbike.repository.TripRentUnitsToChargeMapRepository;
import org.gubbilabs.ecbike.repository.search.TripRentUnitsToChargeMapSearchRepository;
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
 * REST controller for managing TripRentUnitsToChargeMap.
 */
@RestController
@RequestMapping("/api")
public class TripRentUnitsToChargeMapResource {

    private final Logger log = LoggerFactory.getLogger(TripRentUnitsToChargeMapResource.class);
        
    @Inject
    private TripRentUnitsToChargeMapRepository tripRentUnitsToChargeMapRepository;
    
    @Inject
    private TripRentUnitsToChargeMapSearchRepository tripRentUnitsToChargeMapSearchRepository;
    
    /**
     * POST  /trip-rent-units-to-charge-maps : Create a new tripRentUnitsToChargeMap.
     *
     * @param tripRentUnitsToChargeMap the tripRentUnitsToChargeMap to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tripRentUnitsToChargeMap, or with status 400 (Bad Request) if the tripRentUnitsToChargeMap has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trip-rent-units-to-charge-maps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TripRentUnitsToChargeMap> createTripRentUnitsToChargeMap(@Valid @RequestBody TripRentUnitsToChargeMap tripRentUnitsToChargeMap) throws URISyntaxException {
        log.debug("REST request to save TripRentUnitsToChargeMap : {}", tripRentUnitsToChargeMap);
        if (tripRentUnitsToChargeMap.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tripRentUnitsToChargeMap", "idexists", "A new tripRentUnitsToChargeMap cannot already have an ID")).body(null);
        }
        TripRentUnitsToChargeMap result = tripRentUnitsToChargeMapRepository.save(tripRentUnitsToChargeMap);
        tripRentUnitsToChargeMapSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/trip-rent-units-to-charge-maps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tripRentUnitsToChargeMap", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trip-rent-units-to-charge-maps : Updates an existing tripRentUnitsToChargeMap.
     *
     * @param tripRentUnitsToChargeMap the tripRentUnitsToChargeMap to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tripRentUnitsToChargeMap,
     * or with status 400 (Bad Request) if the tripRentUnitsToChargeMap is not valid,
     * or with status 500 (Internal Server Error) if the tripRentUnitsToChargeMap couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trip-rent-units-to-charge-maps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TripRentUnitsToChargeMap> updateTripRentUnitsToChargeMap(@Valid @RequestBody TripRentUnitsToChargeMap tripRentUnitsToChargeMap) throws URISyntaxException {
        log.debug("REST request to update TripRentUnitsToChargeMap : {}", tripRentUnitsToChargeMap);
        if (tripRentUnitsToChargeMap.getId() == null) {
            return createTripRentUnitsToChargeMap(tripRentUnitsToChargeMap);
        }
        TripRentUnitsToChargeMap result = tripRentUnitsToChargeMapRepository.save(tripRentUnitsToChargeMap);
        tripRentUnitsToChargeMapSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tripRentUnitsToChargeMap", tripRentUnitsToChargeMap.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trip-rent-units-to-charge-maps : get all the tripRentUnitsToChargeMaps.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tripRentUnitsToChargeMaps in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/trip-rent-units-to-charge-maps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TripRentUnitsToChargeMap>> getAllTripRentUnitsToChargeMaps(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TripRentUnitsToChargeMaps");
        Page<TripRentUnitsToChargeMap> page = tripRentUnitsToChargeMapRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trip-rent-units-to-charge-maps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trip-rent-units-to-charge-maps/:id : get the "id" tripRentUnitsToChargeMap.
     *
     * @param id the id of the tripRentUnitsToChargeMap to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tripRentUnitsToChargeMap, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/trip-rent-units-to-charge-maps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TripRentUnitsToChargeMap> getTripRentUnitsToChargeMap(@PathVariable Long id) {
        log.debug("REST request to get TripRentUnitsToChargeMap : {}", id);
        TripRentUnitsToChargeMap tripRentUnitsToChargeMap = tripRentUnitsToChargeMapRepository.findOne(id);
        return Optional.ofNullable(tripRentUnitsToChargeMap)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trip-rent-units-to-charge-maps/:id : delete the "id" tripRentUnitsToChargeMap.
     *
     * @param id the id of the tripRentUnitsToChargeMap to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/trip-rent-units-to-charge-maps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTripRentUnitsToChargeMap(@PathVariable Long id) {
        log.debug("REST request to delete TripRentUnitsToChargeMap : {}", id);
        tripRentUnitsToChargeMapRepository.delete(id);
        tripRentUnitsToChargeMapSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tripRentUnitsToChargeMap", id.toString())).build();
    }

    /**
     * SEARCH  /_search/trip-rent-units-to-charge-maps?query=:query : search for the tripRentUnitsToChargeMap corresponding
     * to the query.
     *
     * @param query the query of the tripRentUnitsToChargeMap search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/trip-rent-units-to-charge-maps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TripRentUnitsToChargeMap>> searchTripRentUnitsToChargeMaps(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of TripRentUnitsToChargeMaps for query {}", query);
        Page<TripRentUnitsToChargeMap> page = tripRentUnitsToChargeMapSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/trip-rent-units-to-charge-maps");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
