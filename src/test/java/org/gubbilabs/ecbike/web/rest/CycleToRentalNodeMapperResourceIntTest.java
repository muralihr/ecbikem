package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.CycleToRentalNodeMapper;
import org.gubbilabs.ecbike.repository.CycleToRentalNodeMapperRepository;
import org.gubbilabs.ecbike.repository.search.CycleToRentalNodeMapperSearchRepository;

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
 * Test class for the CycleToRentalNodeMapperResource REST controller.
 *
 * @see CycleToRentalNodeMapperResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class CycleToRentalNodeMapperResourceIntTest {


    @Inject
    private CycleToRentalNodeMapperRepository cycleToRentalNodeMapperRepository;

    @Inject
    private CycleToRentalNodeMapperSearchRepository cycleToRentalNodeMapperSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCycleToRentalNodeMapperMockMvc;

    private CycleToRentalNodeMapper cycleToRentalNodeMapper;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CycleToRentalNodeMapperResource cycleToRentalNodeMapperResource = new CycleToRentalNodeMapperResource();
        ReflectionTestUtils.setField(cycleToRentalNodeMapperResource, "cycleToRentalNodeMapperSearchRepository", cycleToRentalNodeMapperSearchRepository);
        ReflectionTestUtils.setField(cycleToRentalNodeMapperResource, "cycleToRentalNodeMapperRepository", cycleToRentalNodeMapperRepository);
        this.restCycleToRentalNodeMapperMockMvc = MockMvcBuilders.standaloneSetup(cycleToRentalNodeMapperResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cycleToRentalNodeMapperSearchRepository.deleteAll();
        cycleToRentalNodeMapper = new CycleToRentalNodeMapper();
    }

    @Test
    @Transactional
    public void createCycleToRentalNodeMapper() throws Exception {
        int databaseSizeBeforeCreate = cycleToRentalNodeMapperRepository.findAll().size();

        // Create the CycleToRentalNodeMapper

        restCycleToRentalNodeMapperMockMvc.perform(post("/api/cycle-to-rental-node-mappers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cycleToRentalNodeMapper)))
                .andExpect(status().isCreated());

        // Validate the CycleToRentalNodeMapper in the database
        List<CycleToRentalNodeMapper> cycleToRentalNodeMappers = cycleToRentalNodeMapperRepository.findAll();
        assertThat(cycleToRentalNodeMappers).hasSize(databaseSizeBeforeCreate + 1);
        CycleToRentalNodeMapper testCycleToRentalNodeMapper = cycleToRentalNodeMappers.get(cycleToRentalNodeMappers.size() - 1);

        // Validate the CycleToRentalNodeMapper in ElasticSearch
        CycleToRentalNodeMapper cycleToRentalNodeMapperEs = cycleToRentalNodeMapperSearchRepository.findOne(testCycleToRentalNodeMapper.getId());
        assertThat(cycleToRentalNodeMapperEs).isEqualToComparingFieldByField(testCycleToRentalNodeMapper);
    }

    @Test
    @Transactional
    public void getAllCycleToRentalNodeMappers() throws Exception {
        // Initialize the database
        cycleToRentalNodeMapperRepository.saveAndFlush(cycleToRentalNodeMapper);

        // Get all the cycleToRentalNodeMappers
        restCycleToRentalNodeMapperMockMvc.perform(get("/api/cycle-to-rental-node-mappers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cycleToRentalNodeMapper.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCycleToRentalNodeMapper() throws Exception {
        // Initialize the database
        cycleToRentalNodeMapperRepository.saveAndFlush(cycleToRentalNodeMapper);

        // Get the cycleToRentalNodeMapper
        restCycleToRentalNodeMapperMockMvc.perform(get("/api/cycle-to-rental-node-mappers/{id}", cycleToRentalNodeMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cycleToRentalNodeMapper.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCycleToRentalNodeMapper() throws Exception {
        // Get the cycleToRentalNodeMapper
        restCycleToRentalNodeMapperMockMvc.perform(get("/api/cycle-to-rental-node-mappers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCycleToRentalNodeMapper() throws Exception {
        // Initialize the database
        cycleToRentalNodeMapperRepository.saveAndFlush(cycleToRentalNodeMapper);
        cycleToRentalNodeMapperSearchRepository.save(cycleToRentalNodeMapper);
        int databaseSizeBeforeUpdate = cycleToRentalNodeMapperRepository.findAll().size();

        // Update the cycleToRentalNodeMapper
        CycleToRentalNodeMapper updatedCycleToRentalNodeMapper = new CycleToRentalNodeMapper();
        updatedCycleToRentalNodeMapper.setId(cycleToRentalNodeMapper.getId());

        restCycleToRentalNodeMapperMockMvc.perform(put("/api/cycle-to-rental-node-mappers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCycleToRentalNodeMapper)))
                .andExpect(status().isOk());

        // Validate the CycleToRentalNodeMapper in the database
        List<CycleToRentalNodeMapper> cycleToRentalNodeMappers = cycleToRentalNodeMapperRepository.findAll();
        assertThat(cycleToRentalNodeMappers).hasSize(databaseSizeBeforeUpdate);
        CycleToRentalNodeMapper testCycleToRentalNodeMapper = cycleToRentalNodeMappers.get(cycleToRentalNodeMappers.size() - 1);

        // Validate the CycleToRentalNodeMapper in ElasticSearch
        CycleToRentalNodeMapper cycleToRentalNodeMapperEs = cycleToRentalNodeMapperSearchRepository.findOne(testCycleToRentalNodeMapper.getId());
        assertThat(cycleToRentalNodeMapperEs).isEqualToComparingFieldByField(testCycleToRentalNodeMapper);
    }

    @Test
    @Transactional
    public void deleteCycleToRentalNodeMapper() throws Exception {
        // Initialize the database
        cycleToRentalNodeMapperRepository.saveAndFlush(cycleToRentalNodeMapper);
        cycleToRentalNodeMapperSearchRepository.save(cycleToRentalNodeMapper);
        int databaseSizeBeforeDelete = cycleToRentalNodeMapperRepository.findAll().size();

        // Get the cycleToRentalNodeMapper
        restCycleToRentalNodeMapperMockMvc.perform(delete("/api/cycle-to-rental-node-mappers/{id}", cycleToRentalNodeMapper.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cycleToRentalNodeMapperExistsInEs = cycleToRentalNodeMapperSearchRepository.exists(cycleToRentalNodeMapper.getId());
        assertThat(cycleToRentalNodeMapperExistsInEs).isFalse();

        // Validate the database is empty
        List<CycleToRentalNodeMapper> cycleToRentalNodeMappers = cycleToRentalNodeMapperRepository.findAll();
        assertThat(cycleToRentalNodeMappers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCycleToRentalNodeMapper() throws Exception {
        // Initialize the database
        cycleToRentalNodeMapperRepository.saveAndFlush(cycleToRentalNodeMapper);
        cycleToRentalNodeMapperSearchRepository.save(cycleToRentalNodeMapper);

        // Search the cycleToRentalNodeMapper
        restCycleToRentalNodeMapperMockMvc.perform(get("/api/_search/cycle-to-rental-node-mappers?query=id:" + cycleToRentalNodeMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycleToRentalNodeMapper.getId().intValue())));
    }
}
