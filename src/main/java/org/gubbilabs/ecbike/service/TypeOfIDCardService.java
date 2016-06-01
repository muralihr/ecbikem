package org.gubbilabs.ecbike.service;

import org.gubbilabs.ecbike.domain.TypeOfIDCard;
import org.gubbilabs.ecbike.web.rest.dto.TypeOfIDCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing TypeOfIDCard.
 */
public interface TypeOfIDCardService {

    /**
     * Save a typeOfIDCard.
     * 
     * @param typeOfIDCardDTO the entity to save
     * @return the persisted entity
     */
    TypeOfIDCardDTO save(TypeOfIDCardDTO typeOfIDCardDTO);

    /**
     *  Get all the typeOfIDCards.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TypeOfIDCard> findAll(Pageable pageable);

    /**
     *  Get the "id" typeOfIDCard.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    TypeOfIDCardDTO findOne(Long id);

    /**
     *  Delete the "id" typeOfIDCard.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the typeOfIDCard corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<TypeOfIDCard> search(String query, Pageable pageable);
}
