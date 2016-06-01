package org.gubbilabs.ecbike.service.impl;

import org.gubbilabs.ecbike.service.TypeOfIDCardService;
import org.gubbilabs.ecbike.domain.TypeOfIDCard;
import org.gubbilabs.ecbike.repository.TypeOfIDCardRepository;
import org.gubbilabs.ecbike.repository.search.TypeOfIDCardSearchRepository;
import org.gubbilabs.ecbike.web.rest.dto.TypeOfIDCardDTO;
import org.gubbilabs.ecbike.web.rest.mapper.TypeOfIDCardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TypeOfIDCard.
 */
@Service
@Transactional
public class TypeOfIDCardServiceImpl implements TypeOfIDCardService{

    private final Logger log = LoggerFactory.getLogger(TypeOfIDCardServiceImpl.class);
    
    @Inject
    private TypeOfIDCardRepository typeOfIDCardRepository;
    
    @Inject
    private TypeOfIDCardMapper typeOfIDCardMapper;
    
    @Inject
    private TypeOfIDCardSearchRepository typeOfIDCardSearchRepository;
    
    /**
     * Save a typeOfIDCard.
     * 
     * @param typeOfIDCardDTO the entity to save
     * @return the persisted entity
     */
    public TypeOfIDCardDTO save(TypeOfIDCardDTO typeOfIDCardDTO) {
        log.debug("Request to save TypeOfIDCard : {}", typeOfIDCardDTO);
        TypeOfIDCard typeOfIDCard = typeOfIDCardMapper.typeOfIDCardDTOToTypeOfIDCard(typeOfIDCardDTO);
        typeOfIDCard = typeOfIDCardRepository.save(typeOfIDCard);
        TypeOfIDCardDTO result = typeOfIDCardMapper.typeOfIDCardToTypeOfIDCardDTO(typeOfIDCard);
        typeOfIDCardSearchRepository.save(typeOfIDCard);
        return result;
    }

    /**
     *  Get all the typeOfIDCards.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TypeOfIDCard> findAll(Pageable pageable) {
        log.debug("Request to get all TypeOfIDCards");
        Page<TypeOfIDCard> result = typeOfIDCardRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one typeOfIDCard by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TypeOfIDCardDTO findOne(Long id) {
        log.debug("Request to get TypeOfIDCard : {}", id);
        TypeOfIDCard typeOfIDCard = typeOfIDCardRepository.findOne(id);
        TypeOfIDCardDTO typeOfIDCardDTO = typeOfIDCardMapper.typeOfIDCardToTypeOfIDCardDTO(typeOfIDCard);
        return typeOfIDCardDTO;
    }

    /**
     *  Delete the  typeOfIDCard by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeOfIDCard : {}", id);
        typeOfIDCardRepository.delete(id);
        typeOfIDCardSearchRepository.delete(id);
    }

    /**
     * Search for the typeOfIDCard corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TypeOfIDCard> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TypeOfIDCards for query {}", query);
        return typeOfIDCardSearchRepository.search(queryStringQuery(query), pageable);
    }
}
