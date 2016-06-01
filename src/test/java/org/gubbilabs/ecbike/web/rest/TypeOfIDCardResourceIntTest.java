package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.TypeOfIDCard;
import org.gubbilabs.ecbike.repository.TypeOfIDCardRepository;
import org.gubbilabs.ecbike.service.TypeOfIDCardService;
import org.gubbilabs.ecbike.repository.search.TypeOfIDCardSearchRepository;
import org.gubbilabs.ecbike.web.rest.dto.TypeOfIDCardDTO;
import org.gubbilabs.ecbike.web.rest.mapper.TypeOfIDCardMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TypeOfIDCardResource REST controller.
 *
 * @see TypeOfIDCardResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeOfIDCardResourceIntTest {

    private static final String DEFAULT_AUTHORIZED_ID_NAME = "AAAAA";
    private static final String UPDATED_AUTHORIZED_ID_NAME = "BBBBB";
    private static final String DEFAULT_AUTHORIZATION_AGENCY = "AAAAA";
    private static final String UPDATED_AUTHORIZATION_AGENCY = "BBBBB";

    @Inject
    private TypeOfIDCardRepository typeOfIDCardRepository;

    @Inject
    private TypeOfIDCardMapper typeOfIDCardMapper;

    @Inject
    private TypeOfIDCardService typeOfIDCardService;

    @Inject
    private TypeOfIDCardSearchRepository typeOfIDCardSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeOfIDCardMockMvc;

    private TypeOfIDCard typeOfIDCard;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeOfIDCardResource typeOfIDCardResource = new TypeOfIDCardResource();
        ReflectionTestUtils.setField(typeOfIDCardResource, "typeOfIDCardService", typeOfIDCardService);
        ReflectionTestUtils.setField(typeOfIDCardResource, "typeOfIDCardMapper", typeOfIDCardMapper);
        this.restTypeOfIDCardMockMvc = MockMvcBuilders.standaloneSetup(typeOfIDCardResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeOfIDCardSearchRepository.deleteAll();
        typeOfIDCard = new TypeOfIDCard();
        typeOfIDCard.setAuthorizedIDName(DEFAULT_AUTHORIZED_ID_NAME);
        typeOfIDCard.setAuthorizationAgency(DEFAULT_AUTHORIZATION_AGENCY);
    }

    @Test
    @Transactional
    public void createTypeOfIDCard() throws Exception {
        int databaseSizeBeforeCreate = typeOfIDCardRepository.findAll().size();

        // Create the TypeOfIDCard
        TypeOfIDCardDTO typeOfIDCardDTO = typeOfIDCardMapper.typeOfIDCardToTypeOfIDCardDTO(typeOfIDCard);

        restTypeOfIDCardMockMvc.perform(post("/api/type-of-id-cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeOfIDCardDTO)))
                .andExpect(status().isCreated());

        // Validate the TypeOfIDCard in the database
        List<TypeOfIDCard> typeOfIDCards = typeOfIDCardRepository.findAll();
        assertThat(typeOfIDCards).hasSize(databaseSizeBeforeCreate + 1);
        TypeOfIDCard testTypeOfIDCard = typeOfIDCards.get(typeOfIDCards.size() - 1);
        assertThat(testTypeOfIDCard.getAuthorizedIDName()).isEqualTo(DEFAULT_AUTHORIZED_ID_NAME);
        assertThat(testTypeOfIDCard.getAuthorizationAgency()).isEqualTo(DEFAULT_AUTHORIZATION_AGENCY);

        // Validate the TypeOfIDCard in ElasticSearch
        TypeOfIDCard typeOfIDCardEs = typeOfIDCardSearchRepository.findOne(testTypeOfIDCard.getId());
        assertThat(typeOfIDCardEs).isEqualToComparingFieldByField(testTypeOfIDCard);
    }

    @Test
    @Transactional
    public void checkAuthorizedIDNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeOfIDCardRepository.findAll().size();
        // set the field null
        typeOfIDCard.setAuthorizedIDName(null);

        // Create the TypeOfIDCard, which fails.
        TypeOfIDCardDTO typeOfIDCardDTO = typeOfIDCardMapper.typeOfIDCardToTypeOfIDCardDTO(typeOfIDCard);

        restTypeOfIDCardMockMvc.perform(post("/api/type-of-id-cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeOfIDCardDTO)))
                .andExpect(status().isBadRequest());

        List<TypeOfIDCard> typeOfIDCards = typeOfIDCardRepository.findAll();
        assertThat(typeOfIDCards).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorizationAgencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeOfIDCardRepository.findAll().size();
        // set the field null
        typeOfIDCard.setAuthorizationAgency(null);

        // Create the TypeOfIDCard, which fails.
        TypeOfIDCardDTO typeOfIDCardDTO = typeOfIDCardMapper.typeOfIDCardToTypeOfIDCardDTO(typeOfIDCard);

        restTypeOfIDCardMockMvc.perform(post("/api/type-of-id-cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeOfIDCardDTO)))
                .andExpect(status().isBadRequest());

        List<TypeOfIDCard> typeOfIDCards = typeOfIDCardRepository.findAll();
        assertThat(typeOfIDCards).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeOfIDCards() throws Exception {
        // Initialize the database
        typeOfIDCardRepository.saveAndFlush(typeOfIDCard);

        // Get all the typeOfIDCards
        restTypeOfIDCardMockMvc.perform(get("/api/type-of-id-cards?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeOfIDCard.getId().intValue())))
                .andExpect(jsonPath("$.[*].authorizedIDName").value(hasItem(DEFAULT_AUTHORIZED_ID_NAME.toString())))
                .andExpect(jsonPath("$.[*].authorizationAgency").value(hasItem(DEFAULT_AUTHORIZATION_AGENCY.toString())));
    }

    @Test
    @Transactional
    public void getTypeOfIDCard() throws Exception {
        // Initialize the database
        typeOfIDCardRepository.saveAndFlush(typeOfIDCard);

        // Get the typeOfIDCard
        restTypeOfIDCardMockMvc.perform(get("/api/type-of-id-cards/{id}", typeOfIDCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeOfIDCard.getId().intValue()))
            .andExpect(jsonPath("$.authorizedIDName").value(DEFAULT_AUTHORIZED_ID_NAME.toString()))
            .andExpect(jsonPath("$.authorizationAgency").value(DEFAULT_AUTHORIZATION_AGENCY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeOfIDCard() throws Exception {
        // Get the typeOfIDCard
        restTypeOfIDCardMockMvc.perform(get("/api/type-of-id-cards/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeOfIDCard() throws Exception {
        // Initialize the database
        typeOfIDCardRepository.saveAndFlush(typeOfIDCard);
        typeOfIDCardSearchRepository.save(typeOfIDCard);
        int databaseSizeBeforeUpdate = typeOfIDCardRepository.findAll().size();

        // Update the typeOfIDCard
        TypeOfIDCard updatedTypeOfIDCard = new TypeOfIDCard();
        updatedTypeOfIDCard.setId(typeOfIDCard.getId());
        updatedTypeOfIDCard.setAuthorizedIDName(UPDATED_AUTHORIZED_ID_NAME);
        updatedTypeOfIDCard.setAuthorizationAgency(UPDATED_AUTHORIZATION_AGENCY);
        TypeOfIDCardDTO typeOfIDCardDTO = typeOfIDCardMapper.typeOfIDCardToTypeOfIDCardDTO(updatedTypeOfIDCard);

        restTypeOfIDCardMockMvc.perform(put("/api/type-of-id-cards")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeOfIDCardDTO)))
                .andExpect(status().isOk());

        // Validate the TypeOfIDCard in the database
        List<TypeOfIDCard> typeOfIDCards = typeOfIDCardRepository.findAll();
        assertThat(typeOfIDCards).hasSize(databaseSizeBeforeUpdate);
        TypeOfIDCard testTypeOfIDCard = typeOfIDCards.get(typeOfIDCards.size() - 1);
        assertThat(testTypeOfIDCard.getAuthorizedIDName()).isEqualTo(UPDATED_AUTHORIZED_ID_NAME);
        assertThat(testTypeOfIDCard.getAuthorizationAgency()).isEqualTo(UPDATED_AUTHORIZATION_AGENCY);

        // Validate the TypeOfIDCard in ElasticSearch
        TypeOfIDCard typeOfIDCardEs = typeOfIDCardSearchRepository.findOne(testTypeOfIDCard.getId());
        assertThat(typeOfIDCardEs).isEqualToComparingFieldByField(testTypeOfIDCard);
    }

    @Test
    @Transactional
    public void deleteTypeOfIDCard() throws Exception {
        // Initialize the database
        typeOfIDCardRepository.saveAndFlush(typeOfIDCard);
        typeOfIDCardSearchRepository.save(typeOfIDCard);
        int databaseSizeBeforeDelete = typeOfIDCardRepository.findAll().size();

        // Get the typeOfIDCard
        restTypeOfIDCardMockMvc.perform(delete("/api/type-of-id-cards/{id}", typeOfIDCard.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeOfIDCardExistsInEs = typeOfIDCardSearchRepository.exists(typeOfIDCard.getId());
        assertThat(typeOfIDCardExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeOfIDCard> typeOfIDCards = typeOfIDCardRepository.findAll();
        assertThat(typeOfIDCards).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeOfIDCard() throws Exception {
        // Initialize the database
        typeOfIDCardRepository.saveAndFlush(typeOfIDCard);
        typeOfIDCardSearchRepository.save(typeOfIDCard);

        // Search the typeOfIDCard
        restTypeOfIDCardMockMvc.perform(get("/api/_search/type-of-id-cards?query=id:" + typeOfIDCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOfIDCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].authorizedIDName").value(hasItem(DEFAULT_AUTHORIZED_ID_NAME.toString())))
            .andExpect(jsonPath("$.[*].authorizationAgency").value(hasItem(DEFAULT_AUTHORIZATION_AGENCY.toString())));
    }
}
