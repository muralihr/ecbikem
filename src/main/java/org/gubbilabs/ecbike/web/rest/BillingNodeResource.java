package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.BillingNode;
import org.gubbilabs.ecbike.repository.BillingNodeRepository;
import org.gubbilabs.ecbike.repository.search.BillingNodeSearchRepository;
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
 * REST controller for managing BillingNode.
 */
@RestController
@RequestMapping("/api")
public class BillingNodeResource {

    private final Logger log = LoggerFactory.getLogger(BillingNodeResource.class);
        
    @Inject
    private BillingNodeRepository billingNodeRepository;
    
    @Inject
    private BillingNodeSearchRepository billingNodeSearchRepository;
    
    /**
     * POST  /billing-nodes : Create a new billingNode.
     *
     * @param billingNode the billingNode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billingNode, or with status 400 (Bad Request) if the billingNode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billing-nodes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingNode> createBillingNode(@Valid @RequestBody BillingNode billingNode) throws URISyntaxException {
        log.debug("REST request to save BillingNode : {}", billingNode);
        if (billingNode.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("billingNode", "idexists", "A new billingNode cannot already have an ID")).body(null);
        }
        BillingNode result = billingNodeRepository.save(billingNode);
        billingNodeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/billing-nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("billingNode", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /billing-nodes : Updates an existing billingNode.
     *
     * @param billingNode the billingNode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billingNode,
     * or with status 400 (Bad Request) if the billingNode is not valid,
     * or with status 500 (Internal Server Error) if the billingNode couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billing-nodes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingNode> updateBillingNode(@Valid @RequestBody BillingNode billingNode) throws URISyntaxException {
        log.debug("REST request to update BillingNode : {}", billingNode);
        if (billingNode.getId() == null) {
            return createBillingNode(billingNode);
        }
        BillingNode result = billingNodeRepository.save(billingNode);
        billingNodeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("billingNode", billingNode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /billing-nodes : get all the billingNodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of billingNodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/billing-nodes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BillingNode>> getAllBillingNodes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BillingNodes");
        Page<BillingNode> page = billingNodeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/billing-nodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /billing-nodes/:id : get the "id" billingNode.
     *
     * @param id the id of the billingNode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billingNode, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/billing-nodes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BillingNode> getBillingNode(@PathVariable Long id) {
        log.debug("REST request to get BillingNode : {}", id);
        BillingNode billingNode = billingNodeRepository.findOne(id);
        return Optional.ofNullable(billingNode)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /billing-nodes/:id : delete the "id" billingNode.
     *
     * @param id the id of the billingNode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/billing-nodes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBillingNode(@PathVariable Long id) {
        log.debug("REST request to delete BillingNode : {}", id);
        billingNodeRepository.delete(id);
        billingNodeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("billingNode", id.toString())).build();
    }

    /**
     * SEARCH  /_search/billing-nodes?query=:query : search for the billingNode corresponding
     * to the query.
     *
     * @param query the query of the billingNode search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/billing-nodes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BillingNode>> searchBillingNodes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of BillingNodes for query {}", query);
        Page<BillingNode> page = billingNodeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/billing-nodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
