package org.gubbilabs.ecbike.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.gubbilabs.ecbike.domain.MemberMobile;
import org.gubbilabs.ecbike.repository.MemberMobileRepository;
import org.gubbilabs.ecbike.repository.search.MemberMobileSearchRepository;
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
 * REST controller for managing MemberMobile.
 */
@RestController
@RequestMapping("/api")
public class MemberMobileResource {

    private final Logger log = LoggerFactory.getLogger(MemberMobileResource.class);
        
    @Inject
    private MemberMobileRepository memberMobileRepository;
    
    @Inject
    private MemberMobileSearchRepository memberMobileSearchRepository;
    
    /**
     * POST  /member-mobiles : Create a new memberMobile.
     *
     * @param memberMobile the memberMobile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new memberMobile, or with status 400 (Bad Request) if the memberMobile has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/member-mobiles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberMobile> createMemberMobile(@Valid @RequestBody MemberMobile memberMobile) throws URISyntaxException {
        log.debug("REST request to save MemberMobile : {}", memberMobile);
        if (memberMobile.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("memberMobile", "idexists", "A new memberMobile cannot already have an ID")).body(null);
        }
        MemberMobile result = memberMobileRepository.save(memberMobile);
        memberMobileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/member-mobiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("memberMobile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /member-mobiles : Updates an existing memberMobile.
     *
     * @param memberMobile the memberMobile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated memberMobile,
     * or with status 400 (Bad Request) if the memberMobile is not valid,
     * or with status 500 (Internal Server Error) if the memberMobile couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/member-mobiles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberMobile> updateMemberMobile(@Valid @RequestBody MemberMobile memberMobile) throws URISyntaxException {
        log.debug("REST request to update MemberMobile : {}", memberMobile);
        if (memberMobile.getId() == null) {
            return createMemberMobile(memberMobile);
        }
        MemberMobile result = memberMobileRepository.save(memberMobile);
        memberMobileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("memberMobile", memberMobile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /member-mobiles : get all the memberMobiles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of memberMobiles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/member-mobiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberMobile>> getAllMemberMobiles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of MemberMobiles");
        Page<MemberMobile> page = memberMobileRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/member-mobiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /member-mobiles/:id : get the "id" memberMobile.
     *
     * @param id the id of the memberMobile to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the memberMobile, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/member-mobiles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MemberMobile> getMemberMobile(@PathVariable Long id) {
        log.debug("REST request to get MemberMobile : {}", id);
        MemberMobile memberMobile = memberMobileRepository.findOne(id);
        return Optional.ofNullable(memberMobile)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /member-mobiles/:id : delete the "id" memberMobile.
     *
     * @param id the id of the memberMobile to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/member-mobiles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMemberMobile(@PathVariable Long id) {
        log.debug("REST request to delete MemberMobile : {}", id);
        memberMobileRepository.delete(id);
        memberMobileSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("memberMobile", id.toString())).build();
    }

    /**
     * SEARCH  /_search/member-mobiles?query=:query : search for the memberMobile corresponding
     * to the query.
     *
     * @param query the query of the memberMobile search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/member-mobiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MemberMobile>> searchMemberMobiles(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of MemberMobiles for query {}", query);
        Page<MemberMobile> page = memberMobileSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/member-mobiles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
