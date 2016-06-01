package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.TripRentUnitsToChargeMap;
import org.gubbilabs.ecbike.repository.TripRentUnitsToChargeMapRepository;
import org.gubbilabs.ecbike.repository.search.TripRentUnitsToChargeMapSearchRepository;

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
 * Test class for the TripRentUnitsToChargeMapResource REST controller.
 *
 * @see TripRentUnitsToChargeMapResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class TripRentUnitsToChargeMapResourceIntTest {

    private static final String DEFAULT_TYPE_OF_PASS_OR_FARE = "AAAAA";
    private static final String UPDATED_TYPE_OF_PASS_OR_FARE = "BBBBB";

    private static final Integer DEFAULT_PRE_PAID_UNITS = 1;
    private static final Integer UPDATED_PRE_PAID_UNITS = 2;

    private static final Float DEFAULT_CHARGES_FOR_PAID_UNITS = 1F;
    private static final Float UPDATED_CHARGES_FOR_PAID_UNITS = 2F;

    private static final Integer DEFAULT_EXPIRATION_PERIOD_IN_MONTHS = 1;
    private static final Integer UPDATED_EXPIRATION_PERIOD_IN_MONTHS = 2;

    @Inject
    private TripRentUnitsToChargeMapRepository tripRentUnitsToChargeMapRepository;

    @Inject
    private TripRentUnitsToChargeMapSearchRepository tripRentUnitsToChargeMapSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTripRentUnitsToChargeMapMockMvc;

    private TripRentUnitsToChargeMap tripRentUnitsToChargeMap;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TripRentUnitsToChargeMapResource tripRentUnitsToChargeMapResource = new TripRentUnitsToChargeMapResource();
        ReflectionTestUtils.setField(tripRentUnitsToChargeMapResource, "tripRentUnitsToChargeMapSearchRepository", tripRentUnitsToChargeMapSearchRepository);
        ReflectionTestUtils.setField(tripRentUnitsToChargeMapResource, "tripRentUnitsToChargeMapRepository", tripRentUnitsToChargeMapRepository);
        this.restTripRentUnitsToChargeMapMockMvc = MockMvcBuilders.standaloneSetup(tripRentUnitsToChargeMapResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tripRentUnitsToChargeMapSearchRepository.deleteAll();
        tripRentUnitsToChargeMap = new TripRentUnitsToChargeMap();
        tripRentUnitsToChargeMap.setTypeOfPassOrFare(DEFAULT_TYPE_OF_PASS_OR_FARE);
        tripRentUnitsToChargeMap.setPrePaidUnits(DEFAULT_PRE_PAID_UNITS);
        tripRentUnitsToChargeMap.setChargesForPaidUnits(DEFAULT_CHARGES_FOR_PAID_UNITS);
        tripRentUnitsToChargeMap.setExpirationPeriodInMonths(DEFAULT_EXPIRATION_PERIOD_IN_MONTHS);
    }

    @Test
    @Transactional
    public void createTripRentUnitsToChargeMap() throws Exception {
        int databaseSizeBeforeCreate = tripRentUnitsToChargeMapRepository.findAll().size();

        // Create the TripRentUnitsToChargeMap

        restTripRentUnitsToChargeMapMockMvc.perform(post("/api/trip-rent-units-to-charge-maps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tripRentUnitsToChargeMap)))
                .andExpect(status().isCreated());

        // Validate the TripRentUnitsToChargeMap in the database
        List<TripRentUnitsToChargeMap> tripRentUnitsToChargeMaps = tripRentUnitsToChargeMapRepository.findAll();
        assertThat(tripRentUnitsToChargeMaps).hasSize(databaseSizeBeforeCreate + 1);
        TripRentUnitsToChargeMap testTripRentUnitsToChargeMap = tripRentUnitsToChargeMaps.get(tripRentUnitsToChargeMaps.size() - 1);
        assertThat(testTripRentUnitsToChargeMap.getTypeOfPassOrFare()).isEqualTo(DEFAULT_TYPE_OF_PASS_OR_FARE);
        assertThat(testTripRentUnitsToChargeMap.getPrePaidUnits()).isEqualTo(DEFAULT_PRE_PAID_UNITS);
        assertThat(testTripRentUnitsToChargeMap.getChargesForPaidUnits()).isEqualTo(DEFAULT_CHARGES_FOR_PAID_UNITS);
        assertThat(testTripRentUnitsToChargeMap.getExpirationPeriodInMonths()).isEqualTo(DEFAULT_EXPIRATION_PERIOD_IN_MONTHS);

        // Validate the TripRentUnitsToChargeMap in ElasticSearch
        TripRentUnitsToChargeMap tripRentUnitsToChargeMapEs = tripRentUnitsToChargeMapSearchRepository.findOne(testTripRentUnitsToChargeMap.getId());
        assertThat(tripRentUnitsToChargeMapEs).isEqualToComparingFieldByField(testTripRentUnitsToChargeMap);
    }

    @Test
    @Transactional
    public void checkTypeOfPassOrFareIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRentUnitsToChargeMapRepository.findAll().size();
        // set the field null
        tripRentUnitsToChargeMap.setTypeOfPassOrFare(null);

        // Create the TripRentUnitsToChargeMap, which fails.

        restTripRentUnitsToChargeMapMockMvc.perform(post("/api/trip-rent-units-to-charge-maps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tripRentUnitsToChargeMap)))
                .andExpect(status().isBadRequest());

        List<TripRentUnitsToChargeMap> tripRentUnitsToChargeMaps = tripRentUnitsToChargeMapRepository.findAll();
        assertThat(tripRentUnitsToChargeMaps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrePaidUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRentUnitsToChargeMapRepository.findAll().size();
        // set the field null
        tripRentUnitsToChargeMap.setPrePaidUnits(null);

        // Create the TripRentUnitsToChargeMap, which fails.

        restTripRentUnitsToChargeMapMockMvc.perform(post("/api/trip-rent-units-to-charge-maps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tripRentUnitsToChargeMap)))
                .andExpect(status().isBadRequest());

        List<TripRentUnitsToChargeMap> tripRentUnitsToChargeMaps = tripRentUnitsToChargeMapRepository.findAll();
        assertThat(tripRentUnitsToChargeMaps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChargesForPaidUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRentUnitsToChargeMapRepository.findAll().size();
        // set the field null
        tripRentUnitsToChargeMap.setChargesForPaidUnits(null);

        // Create the TripRentUnitsToChargeMap, which fails.

        restTripRentUnitsToChargeMapMockMvc.perform(post("/api/trip-rent-units-to-charge-maps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tripRentUnitsToChargeMap)))
                .andExpect(status().isBadRequest());

        List<TripRentUnitsToChargeMap> tripRentUnitsToChargeMaps = tripRentUnitsToChargeMapRepository.findAll();
        assertThat(tripRentUnitsToChargeMaps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpirationPeriodInMonthsIsRequired() throws Exception {
        int databaseSizeBeforeTest = tripRentUnitsToChargeMapRepository.findAll().size();
        // set the field null
        tripRentUnitsToChargeMap.setExpirationPeriodInMonths(null);

        // Create the TripRentUnitsToChargeMap, which fails.

        restTripRentUnitsToChargeMapMockMvc.perform(post("/api/trip-rent-units-to-charge-maps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tripRentUnitsToChargeMap)))
                .andExpect(status().isBadRequest());

        List<TripRentUnitsToChargeMap> tripRentUnitsToChargeMaps = tripRentUnitsToChargeMapRepository.findAll();
        assertThat(tripRentUnitsToChargeMaps).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTripRentUnitsToChargeMaps() throws Exception {
        // Initialize the database
        tripRentUnitsToChargeMapRepository.saveAndFlush(tripRentUnitsToChargeMap);

        // Get all the tripRentUnitsToChargeMaps
        restTripRentUnitsToChargeMapMockMvc.perform(get("/api/trip-rent-units-to-charge-maps?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tripRentUnitsToChargeMap.getId().intValue())))
                .andExpect(jsonPath("$.[*].typeOfPassOrFare").value(hasItem(DEFAULT_TYPE_OF_PASS_OR_FARE.toString())))
                .andExpect(jsonPath("$.[*].prePaidUnits").value(hasItem(DEFAULT_PRE_PAID_UNITS)))
                .andExpect(jsonPath("$.[*].chargesForPaidUnits").value(hasItem(DEFAULT_CHARGES_FOR_PAID_UNITS.doubleValue())))
                .andExpect(jsonPath("$.[*].expirationPeriodInMonths").value(hasItem(DEFAULT_EXPIRATION_PERIOD_IN_MONTHS)));
    }

    @Test
    @Transactional
    public void getTripRentUnitsToChargeMap() throws Exception {
        // Initialize the database
        tripRentUnitsToChargeMapRepository.saveAndFlush(tripRentUnitsToChargeMap);

        // Get the tripRentUnitsToChargeMap
        restTripRentUnitsToChargeMapMockMvc.perform(get("/api/trip-rent-units-to-charge-maps/{id}", tripRentUnitsToChargeMap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tripRentUnitsToChargeMap.getId().intValue()))
            .andExpect(jsonPath("$.typeOfPassOrFare").value(DEFAULT_TYPE_OF_PASS_OR_FARE.toString()))
            .andExpect(jsonPath("$.prePaidUnits").value(DEFAULT_PRE_PAID_UNITS))
            .andExpect(jsonPath("$.chargesForPaidUnits").value(DEFAULT_CHARGES_FOR_PAID_UNITS.doubleValue()))
            .andExpect(jsonPath("$.expirationPeriodInMonths").value(DEFAULT_EXPIRATION_PERIOD_IN_MONTHS));
    }

    @Test
    @Transactional
    public void getNonExistingTripRentUnitsToChargeMap() throws Exception {
        // Get the tripRentUnitsToChargeMap
        restTripRentUnitsToChargeMapMockMvc.perform(get("/api/trip-rent-units-to-charge-maps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTripRentUnitsToChargeMap() throws Exception {
        // Initialize the database
        tripRentUnitsToChargeMapRepository.saveAndFlush(tripRentUnitsToChargeMap);
        tripRentUnitsToChargeMapSearchRepository.save(tripRentUnitsToChargeMap);
        int databaseSizeBeforeUpdate = tripRentUnitsToChargeMapRepository.findAll().size();

        // Update the tripRentUnitsToChargeMap
        TripRentUnitsToChargeMap updatedTripRentUnitsToChargeMap = new TripRentUnitsToChargeMap();
        updatedTripRentUnitsToChargeMap.setId(tripRentUnitsToChargeMap.getId());
        updatedTripRentUnitsToChargeMap.setTypeOfPassOrFare(UPDATED_TYPE_OF_PASS_OR_FARE);
        updatedTripRentUnitsToChargeMap.setPrePaidUnits(UPDATED_PRE_PAID_UNITS);
        updatedTripRentUnitsToChargeMap.setChargesForPaidUnits(UPDATED_CHARGES_FOR_PAID_UNITS);
        updatedTripRentUnitsToChargeMap.setExpirationPeriodInMonths(UPDATED_EXPIRATION_PERIOD_IN_MONTHS);

        restTripRentUnitsToChargeMapMockMvc.perform(put("/api/trip-rent-units-to-charge-maps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTripRentUnitsToChargeMap)))
                .andExpect(status().isOk());

        // Validate the TripRentUnitsToChargeMap in the database
        List<TripRentUnitsToChargeMap> tripRentUnitsToChargeMaps = tripRentUnitsToChargeMapRepository.findAll();
        assertThat(tripRentUnitsToChargeMaps).hasSize(databaseSizeBeforeUpdate);
        TripRentUnitsToChargeMap testTripRentUnitsToChargeMap = tripRentUnitsToChargeMaps.get(tripRentUnitsToChargeMaps.size() - 1);
        assertThat(testTripRentUnitsToChargeMap.getTypeOfPassOrFare()).isEqualTo(UPDATED_TYPE_OF_PASS_OR_FARE);
        assertThat(testTripRentUnitsToChargeMap.getPrePaidUnits()).isEqualTo(UPDATED_PRE_PAID_UNITS);
        assertThat(testTripRentUnitsToChargeMap.getChargesForPaidUnits()).isEqualTo(UPDATED_CHARGES_FOR_PAID_UNITS);
        assertThat(testTripRentUnitsToChargeMap.getExpirationPeriodInMonths()).isEqualTo(UPDATED_EXPIRATION_PERIOD_IN_MONTHS);

        // Validate the TripRentUnitsToChargeMap in ElasticSearch
        TripRentUnitsToChargeMap tripRentUnitsToChargeMapEs = tripRentUnitsToChargeMapSearchRepository.findOne(testTripRentUnitsToChargeMap.getId());
        assertThat(tripRentUnitsToChargeMapEs).isEqualToComparingFieldByField(testTripRentUnitsToChargeMap);
    }

    @Test
    @Transactional
    public void deleteTripRentUnitsToChargeMap() throws Exception {
        // Initialize the database
        tripRentUnitsToChargeMapRepository.saveAndFlush(tripRentUnitsToChargeMap);
        tripRentUnitsToChargeMapSearchRepository.save(tripRentUnitsToChargeMap);
        int databaseSizeBeforeDelete = tripRentUnitsToChargeMapRepository.findAll().size();

        // Get the tripRentUnitsToChargeMap
        restTripRentUnitsToChargeMapMockMvc.perform(delete("/api/trip-rent-units-to-charge-maps/{id}", tripRentUnitsToChargeMap.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean tripRentUnitsToChargeMapExistsInEs = tripRentUnitsToChargeMapSearchRepository.exists(tripRentUnitsToChargeMap.getId());
        assertThat(tripRentUnitsToChargeMapExistsInEs).isFalse();

        // Validate the database is empty
        List<TripRentUnitsToChargeMap> tripRentUnitsToChargeMaps = tripRentUnitsToChargeMapRepository.findAll();
        assertThat(tripRentUnitsToChargeMaps).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTripRentUnitsToChargeMap() throws Exception {
        // Initialize the database
        tripRentUnitsToChargeMapRepository.saveAndFlush(tripRentUnitsToChargeMap);
        tripRentUnitsToChargeMapSearchRepository.save(tripRentUnitsToChargeMap);

        // Search the tripRentUnitsToChargeMap
        restTripRentUnitsToChargeMapMockMvc.perform(get("/api/_search/trip-rent-units-to-charge-maps?query=id:" + tripRentUnitsToChargeMap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripRentUnitsToChargeMap.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeOfPassOrFare").value(hasItem(DEFAULT_TYPE_OF_PASS_OR_FARE.toString())))
            .andExpect(jsonPath("$.[*].prePaidUnits").value(hasItem(DEFAULT_PRE_PAID_UNITS)))
            .andExpect(jsonPath("$.[*].chargesForPaidUnits").value(hasItem(DEFAULT_CHARGES_FOR_PAID_UNITS.doubleValue())))
            .andExpect(jsonPath("$.[*].expirationPeriodInMonths").value(hasItem(DEFAULT_EXPIRATION_PERIOD_IN_MONTHS)));
    }
}
