package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.RentalTrip;
import org.gubbilabs.ecbike.repository.RentalTripRepository;
import org.gubbilabs.ecbike.repository.search.RentalTripSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RentalTripResource REST controller.
 *
 * @see RentalTripResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class RentalTripResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_RENT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RENT_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RENT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_RENT_START_TIME);

    private static final ZonedDateTime DEFAULT_RENT_STOP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_RENT_STOP_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_RENT_STOP_TIME_STR = dateTimeFormatter.format(DEFAULT_RENT_STOP_TIME);

    private static final Boolean DEFAULT_FINE_APPLIED = false;
    private static final Boolean UPDATED_FINE_APPLIED = true;

    @Inject
    private RentalTripRepository rentalTripRepository;

    @Inject
    private RentalTripSearchRepository rentalTripSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRentalTripMockMvc;

    private RentalTrip rentalTrip;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RentalTripResource rentalTripResource = new RentalTripResource();
        ReflectionTestUtils.setField(rentalTripResource, "rentalTripSearchRepository", rentalTripSearchRepository);
        ReflectionTestUtils.setField(rentalTripResource, "rentalTripRepository", rentalTripRepository);
        this.restRentalTripMockMvc = MockMvcBuilders.standaloneSetup(rentalTripResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        rentalTripSearchRepository.deleteAll();
        rentalTrip = new RentalTrip();
        rentalTrip.setRentStartTime(DEFAULT_RENT_START_TIME);
        rentalTrip.setRentStopTime(DEFAULT_RENT_STOP_TIME);
        rentalTrip.setFineApplied(DEFAULT_FINE_APPLIED);
    }

    @Test
    @Transactional
    public void createRentalTrip() throws Exception {
        int databaseSizeBeforeCreate = rentalTripRepository.findAll().size();

        // Create the RentalTrip

        restRentalTripMockMvc.perform(post("/api/rental-trips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalTrip)))
                .andExpect(status().isCreated());

        // Validate the RentalTrip in the database
        List<RentalTrip> rentalTrips = rentalTripRepository.findAll();
        assertThat(rentalTrips).hasSize(databaseSizeBeforeCreate + 1);
        RentalTrip testRentalTrip = rentalTrips.get(rentalTrips.size() - 1);
        assertThat(testRentalTrip.getRentStartTime()).isEqualTo(DEFAULT_RENT_START_TIME);
        assertThat(testRentalTrip.getRentStopTime()).isEqualTo(DEFAULT_RENT_STOP_TIME);
        assertThat(testRentalTrip.isFineApplied()).isEqualTo(DEFAULT_FINE_APPLIED);

        // Validate the RentalTrip in ElasticSearch
        RentalTrip rentalTripEs = rentalTripSearchRepository.findOne(testRentalTrip.getId());
        assertThat(rentalTripEs).isEqualToComparingFieldByField(testRentalTrip);
    }

    @Test
    @Transactional
    public void checkRentStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalTripRepository.findAll().size();
        // set the field null
        rentalTrip.setRentStartTime(null);

        // Create the RentalTrip, which fails.

        restRentalTripMockMvc.perform(post("/api/rental-trips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalTrip)))
                .andExpect(status().isBadRequest());

        List<RentalTrip> rentalTrips = rentalTripRepository.findAll();
        assertThat(rentalTrips).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRentStopTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalTripRepository.findAll().size();
        // set the field null
        rentalTrip.setRentStopTime(null);

        // Create the RentalTrip, which fails.

        restRentalTripMockMvc.perform(post("/api/rental-trips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalTrip)))
                .andExpect(status().isBadRequest());

        List<RentalTrip> rentalTrips = rentalTripRepository.findAll();
        assertThat(rentalTrips).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRentalTrips() throws Exception {
        // Initialize the database
        rentalTripRepository.saveAndFlush(rentalTrip);

        // Get all the rentalTrips
        restRentalTripMockMvc.perform(get("/api/rental-trips?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rentalTrip.getId().intValue())))
                .andExpect(jsonPath("$.[*].rentStartTime").value(hasItem(DEFAULT_RENT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].rentStopTime").value(hasItem(DEFAULT_RENT_STOP_TIME_STR)))
                .andExpect(jsonPath("$.[*].fineApplied").value(hasItem(DEFAULT_FINE_APPLIED.booleanValue())));
    }

    @Test
    @Transactional
    public void getRentalTrip() throws Exception {
        // Initialize the database
        rentalTripRepository.saveAndFlush(rentalTrip);

        // Get the rentalTrip
        restRentalTripMockMvc.perform(get("/api/rental-trips/{id}", rentalTrip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(rentalTrip.getId().intValue()))
            .andExpect(jsonPath("$.rentStartTime").value(DEFAULT_RENT_START_TIME_STR))
            .andExpect(jsonPath("$.rentStopTime").value(DEFAULT_RENT_STOP_TIME_STR))
            .andExpect(jsonPath("$.fineApplied").value(DEFAULT_FINE_APPLIED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRentalTrip() throws Exception {
        // Get the rentalTrip
        restRentalTripMockMvc.perform(get("/api/rental-trips/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRentalTrip() throws Exception {
        // Initialize the database
        rentalTripRepository.saveAndFlush(rentalTrip);
        rentalTripSearchRepository.save(rentalTrip);
        int databaseSizeBeforeUpdate = rentalTripRepository.findAll().size();

        // Update the rentalTrip
        RentalTrip updatedRentalTrip = new RentalTrip();
        updatedRentalTrip.setId(rentalTrip.getId());
        updatedRentalTrip.setRentStartTime(UPDATED_RENT_START_TIME);
        updatedRentalTrip.setRentStopTime(UPDATED_RENT_STOP_TIME);
        updatedRentalTrip.setFineApplied(UPDATED_FINE_APPLIED);

        restRentalTripMockMvc.perform(put("/api/rental-trips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRentalTrip)))
                .andExpect(status().isOk());

        // Validate the RentalTrip in the database
        List<RentalTrip> rentalTrips = rentalTripRepository.findAll();
        assertThat(rentalTrips).hasSize(databaseSizeBeforeUpdate);
        RentalTrip testRentalTrip = rentalTrips.get(rentalTrips.size() - 1);
        assertThat(testRentalTrip.getRentStartTime()).isEqualTo(UPDATED_RENT_START_TIME);
        assertThat(testRentalTrip.getRentStopTime()).isEqualTo(UPDATED_RENT_STOP_TIME);
        assertThat(testRentalTrip.isFineApplied()).isEqualTo(UPDATED_FINE_APPLIED);

        // Validate the RentalTrip in ElasticSearch
        RentalTrip rentalTripEs = rentalTripSearchRepository.findOne(testRentalTrip.getId());
        assertThat(rentalTripEs).isEqualToComparingFieldByField(testRentalTrip);
    }

    @Test
    @Transactional
    public void deleteRentalTrip() throws Exception {
        // Initialize the database
        rentalTripRepository.saveAndFlush(rentalTrip);
        rentalTripSearchRepository.save(rentalTrip);
        int databaseSizeBeforeDelete = rentalTripRepository.findAll().size();

        // Get the rentalTrip
        restRentalTripMockMvc.perform(delete("/api/rental-trips/{id}", rentalTrip.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean rentalTripExistsInEs = rentalTripSearchRepository.exists(rentalTrip.getId());
        assertThat(rentalTripExistsInEs).isFalse();

        // Validate the database is empty
        List<RentalTrip> rentalTrips = rentalTripRepository.findAll();
        assertThat(rentalTrips).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRentalTrip() throws Exception {
        // Initialize the database
        rentalTripRepository.saveAndFlush(rentalTrip);
        rentalTripSearchRepository.save(rentalTrip);

        // Search the rentalTrip
        restRentalTripMockMvc.perform(get("/api/_search/rental-trips?query=id:" + rentalTrip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalTrip.getId().intValue())))
            .andExpect(jsonPath("$.[*].rentStartTime").value(hasItem(DEFAULT_RENT_START_TIME_STR)))
            .andExpect(jsonPath("$.[*].rentStopTime").value(hasItem(DEFAULT_RENT_STOP_TIME_STR)))
            .andExpect(jsonPath("$.[*].fineApplied").value(hasItem(DEFAULT_FINE_APPLIED.booleanValue())));
    }
}
