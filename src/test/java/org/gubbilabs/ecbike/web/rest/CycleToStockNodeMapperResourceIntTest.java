package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.CycleToStockNodeMapper;
import org.gubbilabs.ecbike.repository.CycleToStockNodeMapperRepository;
import org.gubbilabs.ecbike.repository.search.CycleToStockNodeMapperSearchRepository;

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
 * Test class for the CycleToStockNodeMapperResource REST controller.
 *
 * @see CycleToStockNodeMapperResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class CycleToStockNodeMapperResourceIntTest {


    @Inject
    private CycleToStockNodeMapperRepository cycleToStockNodeMapperRepository;

    @Inject
    private CycleToStockNodeMapperSearchRepository cycleToStockNodeMapperSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCycleToStockNodeMapperMockMvc;

    private CycleToStockNodeMapper cycleToStockNodeMapper;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CycleToStockNodeMapperResource cycleToStockNodeMapperResource = new CycleToStockNodeMapperResource();
        ReflectionTestUtils.setField(cycleToStockNodeMapperResource, "cycleToStockNodeMapperSearchRepository", cycleToStockNodeMapperSearchRepository);
        ReflectionTestUtils.setField(cycleToStockNodeMapperResource, "cycleToStockNodeMapperRepository", cycleToStockNodeMapperRepository);
        this.restCycleToStockNodeMapperMockMvc = MockMvcBuilders.standaloneSetup(cycleToStockNodeMapperResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cycleToStockNodeMapperSearchRepository.deleteAll();
        cycleToStockNodeMapper = new CycleToStockNodeMapper();
    }

    @Test
    @Transactional
    public void createCycleToStockNodeMapper() throws Exception {
        int databaseSizeBeforeCreate = cycleToStockNodeMapperRepository.findAll().size();

        // Create the CycleToStockNodeMapper

        restCycleToStockNodeMapperMockMvc.perform(post("/api/cycle-to-stock-node-mappers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cycleToStockNodeMapper)))
                .andExpect(status().isCreated());

        // Validate the CycleToStockNodeMapper in the database
        List<CycleToStockNodeMapper> cycleToStockNodeMappers = cycleToStockNodeMapperRepository.findAll();
        assertThat(cycleToStockNodeMappers).hasSize(databaseSizeBeforeCreate + 1);
        CycleToStockNodeMapper testCycleToStockNodeMapper = cycleToStockNodeMappers.get(cycleToStockNodeMappers.size() - 1);

        // Validate the CycleToStockNodeMapper in ElasticSearch
        CycleToStockNodeMapper cycleToStockNodeMapperEs = cycleToStockNodeMapperSearchRepository.findOne(testCycleToStockNodeMapper.getId());
        assertThat(cycleToStockNodeMapperEs).isEqualToComparingFieldByField(testCycleToStockNodeMapper);
    }

    @Test
    @Transactional
    public void getAllCycleToStockNodeMappers() throws Exception {
        // Initialize the database
        cycleToStockNodeMapperRepository.saveAndFlush(cycleToStockNodeMapper);

        // Get all the cycleToStockNodeMappers
        restCycleToStockNodeMapperMockMvc.perform(get("/api/cycle-to-stock-node-mappers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cycleToStockNodeMapper.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCycleToStockNodeMapper() throws Exception {
        // Initialize the database
        cycleToStockNodeMapperRepository.saveAndFlush(cycleToStockNodeMapper);

        // Get the cycleToStockNodeMapper
        restCycleToStockNodeMapperMockMvc.perform(get("/api/cycle-to-stock-node-mappers/{id}", cycleToStockNodeMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cycleToStockNodeMapper.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCycleToStockNodeMapper() throws Exception {
        // Get the cycleToStockNodeMapper
        restCycleToStockNodeMapperMockMvc.perform(get("/api/cycle-to-stock-node-mappers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCycleToStockNodeMapper() throws Exception {
        // Initialize the database
        cycleToStockNodeMapperRepository.saveAndFlush(cycleToStockNodeMapper);
        cycleToStockNodeMapperSearchRepository.save(cycleToStockNodeMapper);
        int databaseSizeBeforeUpdate = cycleToStockNodeMapperRepository.findAll().size();

        // Update the cycleToStockNodeMapper
        CycleToStockNodeMapper updatedCycleToStockNodeMapper = new CycleToStockNodeMapper();
        updatedCycleToStockNodeMapper.setId(cycleToStockNodeMapper.getId());

        restCycleToStockNodeMapperMockMvc.perform(put("/api/cycle-to-stock-node-mappers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCycleToStockNodeMapper)))
                .andExpect(status().isOk());

        // Validate the CycleToStockNodeMapper in the database
        List<CycleToStockNodeMapper> cycleToStockNodeMappers = cycleToStockNodeMapperRepository.findAll();
        assertThat(cycleToStockNodeMappers).hasSize(databaseSizeBeforeUpdate);
        CycleToStockNodeMapper testCycleToStockNodeMapper = cycleToStockNodeMappers.get(cycleToStockNodeMappers.size() - 1);

        // Validate the CycleToStockNodeMapper in ElasticSearch
        CycleToStockNodeMapper cycleToStockNodeMapperEs = cycleToStockNodeMapperSearchRepository.findOne(testCycleToStockNodeMapper.getId());
        assertThat(cycleToStockNodeMapperEs).isEqualToComparingFieldByField(testCycleToStockNodeMapper);
    }

    @Test
    @Transactional
    public void deleteCycleToStockNodeMapper() throws Exception {
        // Initialize the database
        cycleToStockNodeMapperRepository.saveAndFlush(cycleToStockNodeMapper);
        cycleToStockNodeMapperSearchRepository.save(cycleToStockNodeMapper);
        int databaseSizeBeforeDelete = cycleToStockNodeMapperRepository.findAll().size();

        // Get the cycleToStockNodeMapper
        restCycleToStockNodeMapperMockMvc.perform(delete("/api/cycle-to-stock-node-mappers/{id}", cycleToStockNodeMapper.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cycleToStockNodeMapperExistsInEs = cycleToStockNodeMapperSearchRepository.exists(cycleToStockNodeMapper.getId());
        assertThat(cycleToStockNodeMapperExistsInEs).isFalse();

        // Validate the database is empty
        List<CycleToStockNodeMapper> cycleToStockNodeMappers = cycleToStockNodeMapperRepository.findAll();
        assertThat(cycleToStockNodeMappers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCycleToStockNodeMapper() throws Exception {
        // Initialize the database
        cycleToStockNodeMapperRepository.saveAndFlush(cycleToStockNodeMapper);
        cycleToStockNodeMapperSearchRepository.save(cycleToStockNodeMapper);

        // Search the cycleToStockNodeMapper
        restCycleToStockNodeMapperMockMvc.perform(get("/api/_search/cycle-to-stock-node-mappers?query=id:" + cycleToStockNodeMapper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycleToStockNodeMapper.getId().intValue())));
    }
}
