package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.gubbilabs.ecbike.domain.Bicycle;
import org.gubbilabs.ecbike.domain.CycleToRentalNodeMapper;
import org.gubbilabs.ecbike.domain.RentalBufferNode;
import org.gubbilabs.ecbike.repository.CycleToRentalNodeMapperRepository;
import org.gubbilabs.ecbike.repository.RentalBufferNodeRepository;
import org.gubbilabs.ecbike.repository.search.RentalBufferNodeSearchRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;
import org.apache.commons.io.FileUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Point;
/**
 * REST controller for managing RentalBufferNode.
 */
@RestController
@RequestMapping("/api")
public class RentalBufferNodeResource {

    private final Logger log = LoggerFactory.getLogger(RentalBufferNodeResource.class);
        
    @Inject
    private RentalBufferNodeRepository rentalBufferNodeRepository;
    
    @Inject
    private RentalBufferNodeSearchRepository rentalBufferNodeSearchRepository;
    
    @Inject
   	private CycleToRentalNodeMapperRepository cycleToRentalNodeMapperRepository;

    
    /**
     * POST  /rental-buffer-nodes : Create a new rentalBufferNode.
     *
     * @param rentalBufferNode the rentalBufferNode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rentalBufferNode, or with status 400 (Bad Request) if the rentalBufferNode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rental-buffer-nodes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RentalBufferNode> createRentalBufferNode(@Valid @RequestBody RentalBufferNode rentalBufferNode) throws URISyntaxException {
        log.debug("REST request to save RentalBufferNode : {}", rentalBufferNode);
        if (rentalBufferNode.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rentalBufferNode", "idexists", "A new rentalBufferNode cannot already have an ID")).body(null);
        }
        RentalBufferNode result = rentalBufferNodeRepository.save(rentalBufferNode);
        rentalBufferNodeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rental-buffer-nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rentalBufferNode", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rental-buffer-nodes : Updates an existing rentalBufferNode.
     *
     * @param rentalBufferNode the rentalBufferNode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rentalBufferNode,
     * or with status 400 (Bad Request) if the rentalBufferNode is not valid,
     * or with status 500 (Internal Server Error) if the rentalBufferNode couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rental-buffer-nodes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RentalBufferNode> updateRentalBufferNode(@Valid @RequestBody RentalBufferNode rentalBufferNode) throws URISyntaxException {
        log.debug("REST request to update RentalBufferNode : {}", rentalBufferNode);
        if (rentalBufferNode.getId() == null) {
            return createRentalBufferNode(rentalBufferNode);
        }
        RentalBufferNode result = rentalBufferNodeRepository.save(rentalBufferNode);
        rentalBufferNodeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rentalBufferNode", rentalBufferNode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rental-buffer-nodes : get all the rentalBufferNodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rentalBufferNodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/rental-buffer-nodes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RentalBufferNode>> getAllRentalBufferNodes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of RentalBufferNodes");
        Page<RentalBufferNode> page = rentalBufferNodeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rental-buffer-nodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    
  
    
    @CrossOrigin
	@RequestMapping(value = "/rental-buffer-nodes/nodemapp", method = { RequestMethod.GET,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	@Transactional(readOnly = true)
	public FeatureCollection getAllHeritageMediaEntitysAsGeoJSON() throws URISyntaxException {

    	  log.debug("REST request to get a page of RentalBufferNodes");
          List<RentalBufferNode> nodeList = rentalBufferNodeRepository.findAllByOrderByIdAsc();
		 
		FeatureCollection totalCollection = convertListToFeatures(nodeList);

		return totalCollection;

	}

    
    private FeatureCollection convertListToFeatures(List<RentalBufferNode> heritageList) {
		FeatureCollection totalCollection = new FeatureCollection();

		for (RentalBufferNode item : heritageList) {

			//first location
			LngLatAlt coordinates = new LngLatAlt(item.getLongitudePos() , item.getLatitudePos());

			Point c = new Point(coordinates);
			Feature f = new Feature();
			f.setGeometry(c);
			f.setId(item.getId().toString());
			Map<String, Object> properties = new HashMap<String, Object>();

			//name and address
			properties.put("title", item.getStationName());
			properties.put("address", item.getAddress1() );
			
			//cycles info;
			//find the number of cycles in the node;			
			//later status;
			
			List<CycleToRentalNodeMapper> cycleToRentalNodeMapper = cycleToRentalNodeMapperRepository.findAllCyclesAtNode(item.getId());
	        List<String> listOfCycles = new LinkedList<String>();
	    	for(CycleToRentalNodeMapper t : cycleToRentalNodeMapper)
	    	{
	    		listOfCycles.add(t.getMovedCycle().getTagId());
	    	}
	    	
			properties.put("cycles", listOfCycles.toString());
			properties.put("cycle-count", listOfCycles.size());

			properties.put("marker-size", "small");	//	
			
			properties.put("marker-color", item.getColorCode()); 
			
			properties.put("marker-symbol", "bike");				 
			f.setProperties(properties);
			totalCollection.add(f);
		}

		return totalCollection;

	}

    /**
     * GET  /rental-buffer-nodes/:id : get the "id" rentalBufferNode.
     *
     * @param id the id of the rentalBufferNode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rentalBufferNode, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/rental-buffer-nodes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RentalBufferNode> getRentalBufferNode(@PathVariable Long id) {
        log.debug("REST request to get RentalBufferNode : {}", id);
        RentalBufferNode rentalBufferNode = rentalBufferNodeRepository.findOne(id);
        return Optional.ofNullable(rentalBufferNode)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rental-buffer-nodes/:id : delete the "id" rentalBufferNode.
     *
     * @param id the id of the rentalBufferNode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/rental-buffer-nodes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRentalBufferNode(@PathVariable Long id) {
        log.debug("REST request to delete RentalBufferNode : {}", id);
        rentalBufferNodeRepository.delete(id);
        rentalBufferNodeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rentalBufferNode", id.toString())).build();
    }

    /**
     * SEARCH  /_search/rental-buffer-nodes?query=:query : search for the rentalBufferNode corresponding
     * to the query.
     *
     * @param query the query of the rentalBufferNode search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/rental-buffer-nodes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<RentalBufferNode>> searchRentalBufferNodes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of RentalBufferNodes for query {}", query);
        Page<RentalBufferNode> page = rentalBufferNodeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/rental-buffer-nodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
