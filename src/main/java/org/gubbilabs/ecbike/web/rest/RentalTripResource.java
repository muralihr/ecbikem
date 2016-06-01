package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.RentalTrip;
import org.gubbilabs.ecbike.domain.util.BetweenDatesJsonObject;
import org.gubbilabs.ecbike.domain.util.FoundTripResponseJsonObject;
import org.gubbilabs.ecbike.domain.util.MissingRentalTripDetails;
import org.gubbilabs.ecbike.domain.util.RentalTripDetails;
import org.gubbilabs.ecbike.domain.util.StartTripMapper;
import org.gubbilabs.ecbike.domain.util.StartTripResponseJsonObject;
import org.gubbilabs.ecbike.domain.util.StopTripMapper;
import org.gubbilabs.ecbike.domain.util.StopTripResponseJsonObject;
import org.gubbilabs.ecbike.repository.RentalTripRepository;
import org.gubbilabs.ecbike.repository.search.RentalTripSearchRepository;
import org.gubbilabs.ecbike.service.RentalTripService;
import org.gubbilabs.ecbike.web.rest.util.HeaderUtil;
import org.gubbilabs.ecbike.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
 * REST controller for managing RentalTrip.
 */
@RestController
@RequestMapping("/api")
public class RentalTripResource {

    private final Logger log = LoggerFactory.getLogger(RentalTripResource.class);
        
    @Inject
    private RentalTripRepository rentalTripRepository;
    
    @Inject
    private RentalTripSearchRepository rentalTripSearchRepository;
    
    @Inject
	private RentalTripService tripService;
    
    /**
     * POST  /rental-trips : Create a new rentalTrip.
     *
     * @param rentalTrip the rentalTrip to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rentalTrip, or with status 400 (Bad Request) if the rentalTrip has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rental-trips",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RentalTrip> createRentalTrip(@Valid @RequestBody RentalTrip rentalTrip) throws URISyntaxException {
        log.debug("REST request to save RentalTrip : {}", rentalTrip);
        if (rentalTrip.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rentalTrip", "idexists", "A new rentalTrip cannot already have an ID")).body(null);
        }
        RentalTrip result = rentalTripRepository.save(rentalTrip);
        rentalTripSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rental-trips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rentalTrip", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rental-trips : Updates an existing rentalTrip.
     *
     * @param rentalTrip the rentalTrip to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rentalTrip,
     * or with status 400 (Bad Request) if the rentalTrip is not valid,
     * or with status 500 (Internal Server Error) if the rentalTrip couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rental-trips",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RentalTrip> updateRentalTrip(@Valid @RequestBody RentalTrip rentalTrip) throws URISyntaxException {
        log.debug("REST request to update RentalTrip : {}", rentalTrip);
        if (rentalTrip.getId() == null) {
            return createRentalTrip(rentalTrip);
        }
        RentalTrip result = rentalTripRepository.save(rentalTrip);
        rentalTripSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rentalTrip", rentalTrip.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rental-trips : get all the rentalTrips.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rentalTrips in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/rental-trips",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RentalTrip>> getAllRentalTrips(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RentalTrips");
        Page<RentalTrip> page = rentalTripRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rental-trips");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /rental-trips/:id : get the "id" rentalTrip.
     *
     * @param id the id of the rentalTrip to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rentalTrip, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/rental-trips/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RentalTrip> getRentalTrip(@PathVariable Long id) {
        log.debug("REST request to get RentalTrip : {}", id);
        RentalTrip rentalTrip = rentalTripRepository.findOne(id);
        return Optional.ofNullable(rentalTrip)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rental-trips/:id : delete the "id" rentalTrip.
     *
     * @param id the id of the rentalTrip to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/rental-trips/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRentalTrip(@PathVariable Long id) {
        log.debug("REST request to delete RentalTrip : {}", id);
        rentalTripRepository.delete(id);
        rentalTripSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rentalTrip", id.toString())).build();
    }

    /**
     * SEARCH  /_search/rental-trips?query=:query : search for the rentalTrip corresponding
     * to the query.
     *
     * @param query the query of the rentalTrip search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/rental-trips",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RentalTrip>> searchRentalTrips(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of RentalTrips for query {}", query);
        Page<RentalTrip> page = rentalTripSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rental-trips");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /////////////////////////////////
    
    /*
	 * Start Trip
	 * 
	 */
	@RequestMapping(value = "/rental-trips/starttrip", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<StartTripResponseJsonObject> startRentalTrip(
			@Valid @RequestBody StartTripMapper startTripMapperObj) throws URISyntaxException {

	
		ResponseEntity<StartTripResponseJsonObject>  startTripResponseJsonObject = tripService.startRentalTrip(startTripMapperObj);
		return startTripResponseJsonObject;
	} 
	
	@RequestMapping(value = "/rental-trips//isMemberValid/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<StartTripResponseJsonObject> isMemberValid(@PathVariable Long id) throws URISyntaxException {

	
		ResponseEntity<StartTripResponseJsonObject>  validChecker = tripService.isMemberValid(id);
		return validChecker;
	} 
	
	@RequestMapping(value = "/rental-trips//findTripByMember/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<FoundTripResponseJsonObject>  findTripByMember(@PathVariable Long id) throws URISyntaxException {

	
		ResponseEntity<FoundTripResponseJsonObject>  validTrip = tripService.findTripByMember(id);
		return validTrip;
	} 
	/*
	 * Stop Trip
	 * 
	 */

	@RequestMapping(value = "/rental-trips//stoptrip", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<StopTripResponseJsonObject> stopRentalTrip(
			@Valid @RequestBody StopTripMapper stopTripMapperObj) throws URISyntaxException {


		ResponseEntity<StopTripResponseJsonObject>  stopTripResponseJsonObject = tripService.stopRentalTrip(stopTripMapperObj);
		return stopTripResponseJsonObject; 
	}
	
	@RequestMapping(value = "/rental-trips//findCompleteTripsByDates",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RentalTripDetails> findCompleteTripsByDates(@Valid @RequestBody  BetweenDatesJsonObject betweenDatesJsonObj) {
		 log.debug("findMissingTripsByDates" +betweenDatesJsonObj.getFromDate(), betweenDatesJsonObj.getToDate());
		 
		 DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		 DateTime fromDate = formatter.parseDateTime(betweenDatesJsonObj.getFromDate());
		 DateTime toDate = formatter.parseDateTime(betweenDatesJsonObj.getToDate());
	     return tripService.findCompleteTripsByDates(fromDate, toDate);
    }
	
	
	@RequestMapping(value = "/rental-trips//findMissingTripsByDates",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MissingRentalTripDetails> findMissingTripsByDates( @Valid @RequestBody  BetweenDatesJsonObject betweenDatesJsonObj) {
        log.debug("findMissingTripsByDates" +betweenDatesJsonObj.getFromDate(), betweenDatesJsonObj.getToDate());
        
		
         DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		 DateTime fromDate = formatter.parseDateTime(betweenDatesJsonObj.getFromDate());
		 DateTime toDate = formatter.parseDateTime(betweenDatesJsonObj.getToDate());
		 return tripService.findMissingTripsByDates(fromDate, toDate);
    } 
	
	@RequestMapping(value = "/rental-trips//findStolenTripsByDates",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MissingRentalTripDetails> findStolenTripsByDates(@Valid @RequestBody  BetweenDatesJsonObject betweenDatesJsonObj)
	{
		 log.debug("findMissingTripsByDates" +betweenDatesJsonObj.getFromDate(), betweenDatesJsonObj.getToDate());
		 
		 
		  DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
			 DateTime fromDate = formatter.parseDateTime(betweenDatesJsonObj.getFromDate());
			 DateTime toDate = formatter.parseDateTime(betweenDatesJsonObj.getToDate());
			 return tripService.findStolenTripsByDates(fromDate, toDate);
	        
    } 


}
