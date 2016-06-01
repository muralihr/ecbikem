package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.HourlyRentalCharges;
import org.gubbilabs.ecbike.repository.HourlyRentalChargesRepository;
import org.gubbilabs.ecbike.repository.search.HourlyRentalChargesSearchRepository;

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
 * Test class for the HourlyRentalChargesResource REST controller.
 *
 * @see HourlyRentalChargesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class HourlyRentalChargesResourceIntTest {


    private static final Integer DEFAULT_DEDUCTABLE_UNITS = 1;
    private static final Integer UPDATED_DEDUCTABLE_UNITS = 2;

    private static final Float DEFAULT_RENTED_HOURS = 1F;
    private static final Float UPDATED_RENTED_HOURS = 2F;

    @Inject
    private HourlyRentalChargesRepository hourlyRentalChargesRepository;

    @Inject
    private HourlyRentalChargesSearchRepository hourlyRentalChargesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHourlyRentalChargesMockMvc;

    private HourlyRentalCharges hourlyRentalCharges;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HourlyRentalChargesResource hourlyRentalChargesResource = new HourlyRentalChargesResource();
        ReflectionTestUtils.setField(hourlyRentalChargesResource, "hourlyRentalChargesSearchRepository", hourlyRentalChargesSearchRepository);
        ReflectionTestUtils.setField(hourlyRentalChargesResource, "hourlyRentalChargesRepository", hourlyRentalChargesRepository);
        this.restHourlyRentalChargesMockMvc = MockMvcBuilders.standaloneSetup(hourlyRentalChargesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hourlyRentalChargesSearchRepository.deleteAll();
        hourlyRentalCharges = new HourlyRentalCharges();
        hourlyRentalCharges.setDeductableUnits(DEFAULT_DEDUCTABLE_UNITS);
        hourlyRentalCharges.setRentedHours(DEFAULT_RENTED_HOURS);
    }

    @Test
    @Transactional
    public void createHourlyRentalCharges() throws Exception {
        int databaseSizeBeforeCreate = hourlyRentalChargesRepository.findAll().size();

        // Create the HourlyRentalCharges

        restHourlyRentalChargesMockMvc.perform(post("/api/hourly-rental-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hourlyRentalCharges)))
                .andExpect(status().isCreated());

        // Validate the HourlyRentalCharges in the database
        List<HourlyRentalCharges> hourlyRentalCharges = hourlyRentalChargesRepository.findAll();
        assertThat(hourlyRentalCharges).hasSize(databaseSizeBeforeCreate + 1);
        HourlyRentalCharges testHourlyRentalCharges = hourlyRentalCharges.get(hourlyRentalCharges.size() - 1);
        assertThat(testHourlyRentalCharges.getDeductableUnits()).isEqualTo(DEFAULT_DEDUCTABLE_UNITS);
        assertThat(testHourlyRentalCharges.getRentedHours()).isEqualTo(DEFAULT_RENTED_HOURS);

        // Validate the HourlyRentalCharges in ElasticSearch
        HourlyRentalCharges hourlyRentalChargesEs = hourlyRentalChargesSearchRepository.findOne(testHourlyRentalCharges.getId());
        assertThat(hourlyRentalChargesEs).isEqualToComparingFieldByField(testHourlyRentalCharges);
    }

    @Test
    @Transactional
    public void checkDeductableUnitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = hourlyRentalChargesRepository.findAll().size();
        // set the field null
        hourlyRentalCharges.setDeductableUnits(null);

        // Create the HourlyRentalCharges, which fails.

        restHourlyRentalChargesMockMvc.perform(post("/api/hourly-rental-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hourlyRentalCharges)))
                .andExpect(status().isBadRequest());

        List<HourlyRentalCharges> hourlyRentalCharges = hourlyRentalChargesRepository.findAll();
        assertThat(hourlyRentalCharges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRentedHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = hourlyRentalChargesRepository.findAll().size();
        // set the field null
        hourlyRentalCharges.setRentedHours(null);

        // Create the HourlyRentalCharges, which fails.

        restHourlyRentalChargesMockMvc.perform(post("/api/hourly-rental-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hourlyRentalCharges)))
                .andExpect(status().isBadRequest());

        List<HourlyRentalCharges> hourlyRentalCharges = hourlyRentalChargesRepository.findAll();
        assertThat(hourlyRentalCharges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHourlyRentalCharges() throws Exception {
        // Initialize the database
        hourlyRentalChargesRepository.saveAndFlush(hourlyRentalCharges);

        // Get all the hourlyRentalCharges
        restHourlyRentalChargesMockMvc.perform(get("/api/hourly-rental-charges?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hourlyRentalCharges.getId().intValue())))
                .andExpect(jsonPath("$.[*].deductableUnits").value(hasItem(DEFAULT_DEDUCTABLE_UNITS)))
                .andExpect(jsonPath("$.[*].rentedHours").value(hasItem(DEFAULT_RENTED_HOURS.doubleValue())));
    }

    @Test
    @Transactional
    public void getHourlyRentalCharges() throws Exception {
        // Initialize the database
        hourlyRentalChargesRepository.saveAndFlush(hourlyRentalCharges);

        // Get the hourlyRentalCharges
        restHourlyRentalChargesMockMvc.perform(get("/api/hourly-rental-charges/{id}", hourlyRentalCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hourlyRentalCharges.getId().intValue()))
            .andExpect(jsonPath("$.deductableUnits").value(DEFAULT_DEDUCTABLE_UNITS))
            .andExpect(jsonPath("$.rentedHours").value(DEFAULT_RENTED_HOURS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHourlyRentalCharges() throws Exception {
        // Get the hourlyRentalCharges
        restHourlyRentalChargesMockMvc.perform(get("/api/hourly-rental-charges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHourlyRentalCharges() throws Exception {
        // Initialize the database
        hourlyRentalChargesRepository.saveAndFlush(hourlyRentalCharges);
        hourlyRentalChargesSearchRepository.save(hourlyRentalCharges);
        int databaseSizeBeforeUpdate = hourlyRentalChargesRepository.findAll().size();

        // Update the hourlyRentalCharges
        HourlyRentalCharges updatedHourlyRentalCharges = new HourlyRentalCharges();
        updatedHourlyRentalCharges.setId(hourlyRentalCharges.getId());
        updatedHourlyRentalCharges.setDeductableUnits(UPDATED_DEDUCTABLE_UNITS);
        updatedHourlyRentalCharges.setRentedHours(UPDATED_RENTED_HOURS);

        restHourlyRentalChargesMockMvc.perform(put("/api/hourly-rental-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHourlyRentalCharges)))
                .andExpect(status().isOk());

        // Validate the HourlyRentalCharges in the database
        List<HourlyRentalCharges> hourlyRentalCharges = hourlyRentalChargesRepository.findAll();
        assertThat(hourlyRentalCharges).hasSize(databaseSizeBeforeUpdate);
        HourlyRentalCharges testHourlyRentalCharges = hourlyRentalCharges.get(hourlyRentalCharges.size() - 1);
        assertThat(testHourlyRentalCharges.getDeductableUnits()).isEqualTo(UPDATED_DEDUCTABLE_UNITS);
        assertThat(testHourlyRentalCharges.getRentedHours()).isEqualTo(UPDATED_RENTED_HOURS);

        // Validate the HourlyRentalCharges in ElasticSearch
        HourlyRentalCharges hourlyRentalChargesEs = hourlyRentalChargesSearchRepository.findOne(testHourlyRentalCharges.getId());
        assertThat(hourlyRentalChargesEs).isEqualToComparingFieldByField(testHourlyRentalCharges);
    }

    @Test
    @Transactional
    public void deleteHourlyRentalCharges() throws Exception {
        // Initialize the database
        hourlyRentalChargesRepository.saveAndFlush(hourlyRentalCharges);
        hourlyRentalChargesSearchRepository.save(hourlyRentalCharges);
        int databaseSizeBeforeDelete = hourlyRentalChargesRepository.findAll().size();

        // Get the hourlyRentalCharges
        restHourlyRentalChargesMockMvc.perform(delete("/api/hourly-rental-charges/{id}", hourlyRentalCharges.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean hourlyRentalChargesExistsInEs = hourlyRentalChargesSearchRepository.exists(hourlyRentalCharges.getId());
        assertThat(hourlyRentalChargesExistsInEs).isFalse();

        // Validate the database is empty
        List<HourlyRentalCharges> hourlyRentalCharges = hourlyRentalChargesRepository.findAll();
        assertThat(hourlyRentalCharges).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHourlyRentalCharges() throws Exception {
        // Initialize the database
        hourlyRentalChargesRepository.saveAndFlush(hourlyRentalCharges);
        hourlyRentalChargesSearchRepository.save(hourlyRentalCharges);

        // Search the hourlyRentalCharges
        restHourlyRentalChargesMockMvc.perform(get("/api/_search/hourly-rental-charges?query=id:" + hourlyRentalCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hourlyRentalCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].deductableUnits").value(hasItem(DEFAULT_DEDUCTABLE_UNITS)))
            .andExpect(jsonPath("$.[*].rentedHours").value(hasItem(DEFAULT_RENTED_HOURS.doubleValue())));
    }
}
