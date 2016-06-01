package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.CycleToCustomerMapper;
import org.gubbilabs.ecbike.repository.CycleToCustomerMapperRepository;
import org.gubbilabs.ecbike.repository.search.CycleToCustomerMapperSearchRepository;

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
 * Test class for the CycleToCustomerMapperResource REST controller.
 *
 * @see CycleToCustomerMapperResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class CycleToCustomerMapperResourceIntTest {


    @Inject
    private CycleToCustomerMapperRepository cycleToCustomerMapperRepository;

    @Inject
    private CycleToCustomerMapperSearchRepository cycleToCustomerMapperSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCycleToCustomerMapperMockMvc;

    private CycleToCustomerMapper cycleToCustomerMapper;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CycleToCustomerMapperResource cycleToCustomerMapperResource = new CycleToCustomerMapperResource();
        ReflectionTestUtils.setField(cycleToCustomerMapperResource, "cycleToCustomerMapperSearchRepository", cycleToCustomerMapperSearchRepository);
        ReflectionTestUtils.setField(cycleToCustomerMapperResource, "cycleToCustomerMapperRepository", cycleToCustomerMapperRepository);
        this.restCycleToCustomerMapperMockMvc = MockMvcBuilders.standaloneSetup(cycleToCustomerMapperResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cycleToCustomerMapperSearchRepository.deleteAll();
        cycleToCustomerMapper = new CycleToCustomerMapper();
    }

    @Test
    @Transactional
    public void createCycleToCustomerMapper() throws Exception {
        int databaseSizeBeforeCreate = cycleToCustomerMapperRepository.findAll().size();

        // Create the CycleToCustomerMapper

        restCycleToCustomerMapperMockMvc.perform(post("/api/cycle-to-customer-mappers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cycleToCustomerMapper)))
                .andExpect(status().isCreated());

        // Validate the CycleToCustomerMapper in the database
        List<CycleToCustomerMapper> cycleToCustomerMappers = cycleToCustomerMapperRepository.findAll();
        assertThat(cycleToCustomerMappers).hasSize(databaseSizeBeforeCreate + 1);
        CycleToCustomerMapper testCycleToCustomerMapper = cycleToCustomerMappers.get(cycleToCustomerMappers.size() - 1);

        // Validate the CycleToCustomerMapper in ElasticSearch
        CycleToCustomerMapper cycleToCustomerMapperEs = cycleToCustomerMapperSearchRepository.findOne(testCycleToCustomerMapper.getId());
        assertThat(cycleToCustomerMapperEs).isEqualToComparingFieldByField(testCycleToCustomerMapper);
    }

    @Test
    @Transactional
    public void getAllCycleToCustomerMappers() throws Exception {
        // Initialize the database
        cycleToCustomerMapperRepository.saveAndFlush(cycleToCustomerMapper);

        // Get all the cycleToCustomerMappers
        restCycleToCustomerMapperMockMvc.perform(get("/api/cycle-to-customer-mappers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cycleToCustomerMapper.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCycleToCustomerMapper() throws Exception {
        // Initialize the database
        cycleToCustomerMapperRepository.saveAndFlush(cycleToCustomerMapper);

        // Get the cycleToCustomerMapper
        restCycleToCustomerMapperMockMvc.perform(get("/api/cycle-to-customer-mappers/{id}", cycleToCustomerMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cycleToCustomerMapper.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCycleToCustomerMapper() throws Exception {
        // Get the cycleToCustomerMapper
        restCycleToCustomerMapperMockMvc.perform(get("/api/cycle-to-customer-mappers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCycleToCustomerMapper() throws Exception {
        // Initialize the database
        cycleToCustomerMapperRepository.saveAndFlush(cycleToCustomerMapper);
        cycleToCustomerMapperSearchRepository.save(cycleToCustomerMapper);
        int databaseSizeBeforeUpdate = cycleToCustomerMapperRepository.findAll().size();

        // Update the cycleToCustomerMapper
        CycleToCustomerMapper updatedCycleToCustomerMapper = new CycleToCustomerMapper();
        updatedCycleToCustomerMapper.setId(cycleToCustomerMapper.getId());

        restCycleToCustomerMapperMockMvc.perform(put("/api/cycle-to-customer-mappers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCycleToCustomerMapper)))
                .andExpect(status().isOk());

        // Validate the CycleToCustomerMapper in the database
        List<CycleToCustomerMapper> cycleToCustomerMappers = cycleToCustomerMapperRepository.findAll();
        assertThat(cycleToCustomerMappers).hasSize(databaseSizeBeforeUpdate);
        CycleToCustomerMapper testCycleToCustomerMapper = cycleToCustomerMappers.get(cycleToCustomerMappers.size() - 1);

        // Validate the CycleToCustomerMapper in ElasticSearch
        CycleToCustomerMapper cycleToCustomerMapperEs = cycleToCustomerMapperSearchRepository.findOne(testCycleToCustomerMapper.getId());
        assertThat(cycleToCustomerMapperEs).isEqualToComparingFieldByField(testCycleToCustomerMapper);
    }

    @Test
    @Transactional
    public void deleteCycleToCustomerMapper() throws Exception {
        // Initialize the database
        cycleToCustomerMapperRepository.saveAndFlush(cycleToCustomerMapper);
        cycleToCustomerMapperSearchRepository.save(cycleToCustomerMapper);
        int databaseSizeBeforeDelete = cycleToCustomerMapperRepository.findAll().size();

        // Get the cycleToCustomerMapper
        restCycleToCustomerMapperMockMvc.perform(delete("/api/cycle-to-customer-mappers/{id}", cycleToCustomerMapper.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cycleToCustomerMapperExistsInEs = cycleToCustomerMapperSearchRepository.exists(cycleToCustomerMapper.getId());
        assertThat(cycleToCustomerMapperExistsInEs).isFalse();

        // Validate the database is empty
        List<CycleToCustomerMapper> cycleToCustomerMappers = cycleToCustomerMapperRepository.findAll();
        assertThat(cycleToCustomerMappers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCycleToCustomerMapper() throws Exception {
        // Initialize the database
        cycleToCustomerMapperRepository.saveAndFlush(cycleToCustomerMapper);
        cycleToCustomerMapperSearchRepository.save(cycleToCustomerMapper);

        // Search the cycleToCustomerMapper
        restCycleToCustomerMapperMockMvc.perform(get("/api/_search/cycle-to-customer-mappers?query=id:" + cycleToCustomerMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycleToCustomerMapper.getId().intValue())));
    }
}
