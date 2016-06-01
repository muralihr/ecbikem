package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.MemberBill;
import org.gubbilabs.ecbike.repository.MemberBillRepository;
import org.gubbilabs.ecbike.repository.search.MemberBillSearchRepository;
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
 * REST controller for managing MemberBill.
 */
@RestController
@RequestMapping("/api")
public class MemberBillResource {

    private final Logger log = LoggerFactory.getLogger(MemberBillResource.class);
        
    @Inject
    private MemberBillRepository memberBillRepository;
    
    @Inject
    private MemberBillSearchRepository memberBillSearchRepository;
    
    /**
     * POST  /member-bills : Create a new memberBill.
     *
     * @param memberBill the memberBill to create
     * @return the ResponseEntity with status 201 (Created) and with body the new memberBill, or with status 400 (Bad Request) if the memberBill has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/member-bills",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberBill> createMemberBill(@Valid @RequestBody MemberBill memberBill) throws URISyntaxException {
        log.debug("REST request to save MemberBill : {}", memberBill);
        if (memberBill.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("memberBill", "idexists", "A new memberBill cannot already have an ID")).body(null);
        }
        MemberBill result = memberBillRepository.save(memberBill);
        memberBillSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/member-bills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("memberBill", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /member-bills : Updates an existing memberBill.
     *
     * @param memberBill the memberBill to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated memberBill,
     * or with status 400 (Bad Request) if the memberBill is not valid,
     * or with status 500 (Internal Server Error) if the memberBill couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/member-bills",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberBill> updateMemberBill(@Valid @RequestBody MemberBill memberBill) throws URISyntaxException {
        log.debug("REST request to update MemberBill : {}", memberBill);
        if (memberBill.getId() == null) {
            return createMemberBill(memberBill);
        }
        MemberBill result = memberBillRepository.save(memberBill);
        memberBillSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("memberBill", memberBill.getId().toString()))
            .body(result);
    }

    /**
     * GET  /member-bills : get all the memberBills.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of memberBills in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/member-bills",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberBill>> getAllMemberBills(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MemberBills");
        Page<MemberBill> page = memberBillRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/member-bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /member-bills/:id : get the "id" memberBill.
     *
     * @param id the id of the memberBill to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the memberBill, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/member-bills/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberBill> getMemberBill(@PathVariable Long id) {
        log.debug("REST request to get MemberBill : {}", id);
        MemberBill memberBill = memberBillRepository.findOne(id);
        return Optional.ofNullable(memberBill)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /member-bills/:id : delete the "id" memberBill.
     *
     * @param id the id of the memberBill to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/member-bills/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMemberBill(@PathVariable Long id) {
        log.debug("REST request to delete MemberBill : {}", id);
        memberBillRepository.delete(id);
        memberBillSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("memberBill", id.toString())).build();
    }

    /**
     * SEARCH  /_search/member-bills?query=:query : search for the memberBill corresponding
     * to the query.
     *
     * @param query the query of the memberBill search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/member-bills",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberBill>> searchMemberBills(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of MemberBills for query {}", query);
        Page<MemberBill> page = memberBillSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/member-bills");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
