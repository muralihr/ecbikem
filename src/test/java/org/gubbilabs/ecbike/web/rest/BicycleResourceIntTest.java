package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.Bicycle;
import org.gubbilabs.ecbike.repository.BicycleRepository;
import org.gubbilabs.ecbike.repository.search.BicycleSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BicycleResource REST controller.
 *
 * @see BicycleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class BicycleResourceIntTest {

    private static final String DEFAULT_TAG_ID = "AAAAA";
    private static final String UPDATED_TAG_ID = "BBBBB";
    private static final String DEFAULT_CYCLE_MANUFACTURER = "A";
    private static final String UPDATED_CYCLE_MANUFACTURER = "B";

    private static final LocalDate DEFAULT_DATE_OF_PURCHASE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_PURCHASE = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_COST_OF_CYCLE = 1F;
    private static final Float UPDATED_COST_OF_CYCLE = 2F;
    private static final String DEFAULT_INSURANCE_NO = "AAAAA";
    private static final String UPDATED_INSURANCE_NO = "BBBBB";

    private static final Integer DEFAULT_MOVE_STATUS = 1;
    private static final Integer UPDATED_MOVE_STATUS = 2;

    private static final Integer DEFAULT_INSURANCE_STATUS = 1;
    private static final Integer UPDATED_INSURANCE_STATUS = 2;

    @Inject
    private BicycleRepository bicycleRepository;

    @Inject
    private BicycleSearchRepository bicycleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBicycleMockMvc;

    private Bicycle bicycle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BicycleResource bicycleResource = new BicycleResource();
        ReflectionTestUtils.setField(bicycleResource, "bicycleSearchRepository", bicycleSearchRepository);
        ReflectionTestUtils.setField(bicycleResource, "bicycleRepository", bicycleRepository);
        this.restBicycleMockMvc = MockMvcBuilders.standaloneSetup(bicycleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bicycleSearchRepository.deleteAll();
        bicycle = new Bicycle();
        bicycle.setTagId(DEFAULT_TAG_ID);
        bicycle.setCycleManufacturer(DEFAULT_CYCLE_MANUFACTURER);
        bicycle.setDateOfPurchase(DEFAULT_DATE_OF_PURCHASE);
        bicycle.setCostOfCycle(DEFAULT_COST_OF_CYCLE);
        bicycle.setInsuranceNo(DEFAULT_INSURANCE_NO);
        bicycle.setMoveStatus(DEFAULT_MOVE_STATUS);
        bicycle.setInsuranceStatus(DEFAULT_INSURANCE_STATUS);
    }

    @Test
    @Transactional
    public void createBicycle() throws Exception {
        int databaseSizeBeforeCreate = bicycleRepository.findAll().size();

        // Create the Bicycle

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isCreated());

        // Validate the Bicycle in the database
        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeCreate + 1);
        Bicycle testBicycle = bicycles.get(bicycles.size() - 1);
        assertThat(testBicycle.getTagId()).isEqualTo(DEFAULT_TAG_ID);
        assertThat(testBicycle.getCycleManufacturer()).isEqualTo(DEFAULT_CYCLE_MANUFACTURER);
        assertThat(testBicycle.getDateOfPurchase()).isEqualTo(DEFAULT_DATE_OF_PURCHASE);
        assertThat(testBicycle.getCostOfCycle()).isEqualTo(DEFAULT_COST_OF_CYCLE);
        assertThat(testBicycle.getInsuranceNo()).isEqualTo(DEFAULT_INSURANCE_NO);
        assertThat(testBicycle.getMoveStatus()).isEqualTo(DEFAULT_MOVE_STATUS);
        assertThat(testBicycle.getInsuranceStatus()).isEqualTo(DEFAULT_INSURANCE_STATUS);

        // Validate the Bicycle in ElasticSearch
        Bicycle bicycleEs = bicycleSearchRepository.findOne(testBicycle.getId());
        assertThat(bicycleEs).isEqualToComparingFieldByField(testBicycle);
    }

    @Test
    @Transactional
    public void checkTagIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bicycleRepository.findAll().size();
        // set the field null
        bicycle.setTagId(null);

        // Create the Bicycle, which fails.

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isBadRequest());

        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCycleManufacturerIsRequired() throws Exception {
        int databaseSizeBeforeTest = bicycleRepository.findAll().size();
        // set the field null
        bicycle.setCycleManufacturer(null);

        // Create the Bicycle, which fails.

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isBadRequest());

        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOfPurchaseIsRequired() throws Exception {
        int databaseSizeBeforeTest = bicycleRepository.findAll().size();
        // set the field null
        bicycle.setDateOfPurchase(null);

        // Create the Bicycle, which fails.

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isBadRequest());

        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCostOfCycleIsRequired() throws Exception {
        int databaseSizeBeforeTest = bicycleRepository.findAll().size();
        // set the field null
        bicycle.setCostOfCycle(null);

        // Create the Bicycle, which fails.

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isBadRequest());

        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInsuranceNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = bicycleRepository.findAll().size();
        // set the field null
        bicycle.setInsuranceNo(null);

        // Create the Bicycle, which fails.

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isBadRequest());

        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMoveStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bicycleRepository.findAll().size();
        // set the field null
        bicycle.setMoveStatus(null);

        // Create the Bicycle, which fails.

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isBadRequest());

        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInsuranceStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bicycleRepository.findAll().size();
        // set the field null
        bicycle.setInsuranceStatus(null);

        // Create the Bicycle, which fails.

        restBicycleMockMvc.perform(post("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bicycle)))
                .andExpect(status().isBadRequest());

        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBicycles() throws Exception {
        // Initialize the database
        bicycleRepository.saveAndFlush(bicycle);

        // Get all the bicycles
        restBicycleMockMvc.perform(get("/api/bicycles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bicycle.getId().intValue())))
                .andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID.toString())))
                .andExpect(jsonPath("$.[*].cycleManufacturer").value(hasItem(DEFAULT_CYCLE_MANUFACTURER.toString())))
                .andExpect(jsonPath("$.[*].dateOfPurchase").value(hasItem(DEFAULT_DATE_OF_PURCHASE.toString())))
                .andExpect(jsonPath("$.[*].costOfCycle").value(hasItem(DEFAULT_COST_OF_CYCLE.doubleValue())))
                .andExpect(jsonPath("$.[*].insuranceNo").value(hasItem(DEFAULT_INSURANCE_NO.toString())))
                .andExpect(jsonPath("$.[*].moveStatus").value(hasItem(DEFAULT_MOVE_STATUS)))
                .andExpect(jsonPath("$.[*].insuranceStatus").value(hasItem(DEFAULT_INSURANCE_STATUS)));
    }

    @Test
    @Transactional
    public void getBicycle() throws Exception {
        // Initialize the database
        bicycleRepository.saveAndFlush(bicycle);

        // Get the bicycle
        restBicycleMockMvc.perform(get("/api/bicycles/{id}", bicycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bicycle.getId().intValue()))
            .andExpect(jsonPath("$.tagId").value(DEFAULT_TAG_ID.toString()))
            .andExpect(jsonPath("$.cycleManufacturer").value(DEFAULT_CYCLE_MANUFACTURER.toString()))
            .andExpect(jsonPath("$.dateOfPurchase").value(DEFAULT_DATE_OF_PURCHASE.toString()))
            .andExpect(jsonPath("$.costOfCycle").value(DEFAULT_COST_OF_CYCLE.doubleValue()))
            .andExpect(jsonPath("$.insuranceNo").value(DEFAULT_INSURANCE_NO.toString()))
            .andExpect(jsonPath("$.moveStatus").value(DEFAULT_MOVE_STATUS))
            .andExpect(jsonPath("$.insuranceStatus").value(DEFAULT_INSURANCE_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingBicycle() throws Exception {
        // Get the bicycle
        restBicycleMockMvc.perform(get("/api/bicycles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBicycle() throws Exception {
        // Initialize the database
        bicycleRepository.saveAndFlush(bicycle);
        bicycleSearchRepository.save(bicycle);
        int databaseSizeBeforeUpdate = bicycleRepository.findAll().size();

        // Update the bicycle
        Bicycle updatedBicycle = new Bicycle();
        updatedBicycle.setId(bicycle.getId());
        updatedBicycle.setTagId(UPDATED_TAG_ID);
        updatedBicycle.setCycleManufacturer(UPDATED_CYCLE_MANUFACTURER);
        updatedBicycle.setDateOfPurchase(UPDATED_DATE_OF_PURCHASE);
        updatedBicycle.setCostOfCycle(UPDATED_COST_OF_CYCLE);
        updatedBicycle.setInsuranceNo(UPDATED_INSURANCE_NO);
        updatedBicycle.setMoveStatus(UPDATED_MOVE_STATUS);
        updatedBicycle.setInsuranceStatus(UPDATED_INSURANCE_STATUS);

        restBicycleMockMvc.perform(put("/api/bicycles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBicycle)))
                .andExpect(status().isOk());

        // Validate the Bicycle in the database
        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeUpdate);
        Bicycle testBicycle = bicycles.get(bicycles.size() - 1);
        assertThat(testBicycle.getTagId()).isEqualTo(UPDATED_TAG_ID);
        assertThat(testBicycle.getCycleManufacturer()).isEqualTo(UPDATED_CYCLE_MANUFACTURER);
        assertThat(testBicycle.getDateOfPurchase()).isEqualTo(UPDATED_DATE_OF_PURCHASE);
        assertThat(testBicycle.getCostOfCycle()).isEqualTo(UPDATED_COST_OF_CYCLE);
        assertThat(testBicycle.getInsuranceNo()).isEqualTo(UPDATED_INSURANCE_NO);
        assertThat(testBicycle.getMoveStatus()).isEqualTo(UPDATED_MOVE_STATUS);
        assertThat(testBicycle.getInsuranceStatus()).isEqualTo(UPDATED_INSURANCE_STATUS);

        // Validate the Bicycle in ElasticSearch
        Bicycle bicycleEs = bicycleSearchRepository.findOne(testBicycle.getId());
        assertThat(bicycleEs).isEqualToComparingFieldByField(testBicycle);
    }

    @Test
    @Transactional
    public void deleteBicycle() throws Exception {
        // Initialize the database
        bicycleRepository.saveAndFlush(bicycle);
        bicycleSearchRepository.save(bicycle);
        int databaseSizeBeforeDelete = bicycleRepository.findAll().size();

        // Get the bicycle
        restBicycleMockMvc.perform(delete("/api/bicycles/{id}", bicycle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bicycleExistsInEs = bicycleSearchRepository.exists(bicycle.getId());
        assertThat(bicycleExistsInEs).isFalse();

        // Validate the database is empty
        List<Bicycle> bicycles = bicycleRepository.findAll();
        assertThat(bicycles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBicycle() throws Exception {
        // Initialize the database
        bicycleRepository.saveAndFlush(bicycle);
        bicycleSearchRepository.save(bicycle);

        // Search the bicycle
        restBicycleMockMvc.perform(get("/api/_search/bicycles?query=id:" + bicycle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bicycle.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagId").value(hasItem(DEFAULT_TAG_ID.toString())))
            .andExpect(jsonPath("$.[*].cycleManufacturer").value(hasItem(DEFAULT_CYCLE_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].dateOfPurchase").value(hasItem(DEFAULT_DATE_OF_PURCHASE.toString())))
            .andExpect(jsonPath("$.[*].costOfCycle").value(hasItem(DEFAULT_COST_OF_CYCLE.doubleValue())))
            .andExpect(jsonPath("$.[*].insuranceNo").value(hasItem(DEFAULT_INSURANCE_NO.toString())))
            .andExpect(jsonPath("$.[*].moveStatus").value(hasItem(DEFAULT_MOVE_STATUS)))
            .andExpect(jsonPath("$.[*].insuranceStatus").value(hasItem(DEFAULT_INSURANCE_STATUS)));
    }
}
