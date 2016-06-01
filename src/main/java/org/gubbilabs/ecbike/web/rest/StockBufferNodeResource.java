package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.StockBufferNode;
import org.gubbilabs.ecbike.repository.StockBufferNodeRepository;
import org.gubbilabs.ecbike.repository.search.StockBufferNodeSearchRepository;
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
 * REST controller for managing StockBufferNode.
 */
@RestController
@RequestMapping("/api")
public class StockBufferNodeResource {

    private final Logger log = LoggerFactory.getLogger(StockBufferNodeResource.class);
        
    @Inject
    private StockBufferNodeRepository stockBufferNodeRepository;
    
    @Inject
    private StockBufferNodeSearchRepository stockBufferNodeSearchRepository;
    
    /**
     * POST  /stock-buffer-nodes : Create a new stockBufferNode.
     *
     * @param stockBufferNode the stockBufferNode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockBufferNode, or with status 400 (Bad Request) if the stockBufferNode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stock-buffer-nodes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockBufferNode> createStockBufferNode(@Valid @RequestBody StockBufferNode stockBufferNode) throws URISyntaxException {
        log.debug("REST request to save StockBufferNode : {}", stockBufferNode);
        if (stockBufferNode.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("stockBufferNode", "idexists", "A new stockBufferNode cannot already have an ID")).body(null);
        }
        StockBufferNode result = stockBufferNodeRepository.save(stockBufferNode);
        stockBufferNodeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/stock-buffer-nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("stockBufferNode", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-buffer-nodes : Updates an existing stockBufferNode.
     *
     * @param stockBufferNode the stockBufferNode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockBufferNode,
     * or with status 400 (Bad Request) if the stockBufferNode is not valid,
     * or with status 500 (Internal Server Error) if the stockBufferNode couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/stock-buffer-nodes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockBufferNode> updateStockBufferNode(@Valid @RequestBody StockBufferNode stockBufferNode) throws URISyntaxException {
        log.debug("REST request to update StockBufferNode : {}", stockBufferNode);
        if (stockBufferNode.getId() == null) {
            return createStockBufferNode(stockBufferNode);
        }
        StockBufferNode result = stockBufferNodeRepository.save(stockBufferNode);
        stockBufferNodeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("stockBufferNode", stockBufferNode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-buffer-nodes : get all the stockBufferNodes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockBufferNodes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/stock-buffer-nodes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StockBufferNode>> getAllStockBufferNodes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StockBufferNodes");
        Page<StockBufferNode> page = stockBufferNodeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-buffer-nodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /stock-buffer-nodes/:id : get the "id" stockBufferNode.
     *
     * @param id the id of the stockBufferNode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockBufferNode, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/stock-buffer-nodes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StockBufferNode> getStockBufferNode(@PathVariable Long id) {
        log.debug("REST request to get StockBufferNode : {}", id);
        StockBufferNode stockBufferNode = stockBufferNodeRepository.findOne(id);
        return Optional.ofNullable(stockBufferNode)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /stock-buffer-nodes/:id : delete the "id" stockBufferNode.
     *
     * @param id the id of the stockBufferNode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/stock-buffer-nodes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStockBufferNode(@PathVariable Long id) {
        log.debug("REST request to delete StockBufferNode : {}", id);
        stockBufferNodeRepository.delete(id);
        stockBufferNodeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("stockBufferNode", id.toString())).build();
    }

    /**
     * SEARCH  /_search/stock-buffer-nodes?query=:query : search for the stockBufferNode corresponding
     * to the query.
     *
     * @param query the query of the stockBufferNode search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/stock-buffer-nodes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StockBufferNode>> searchStockBufferNodes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of StockBufferNodes for query {}", query);
        Page<StockBufferNode> page = stockBufferNodeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/stock-buffer-nodes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
