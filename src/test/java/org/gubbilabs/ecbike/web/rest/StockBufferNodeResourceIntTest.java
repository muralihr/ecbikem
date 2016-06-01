package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.StockBufferNode;
import org.gubbilabs.ecbike.repository.StockBufferNodeRepository;
import org.gubbilabs.ecbike.repository.search.StockBufferNodeSearchRepository;

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
 * Test class for the StockBufferNodeResource REST controller.
 *
 * @see StockBufferNodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class StockBufferNodeResourceIntTest {

    private static final String DEFAULT_GODOWN_NAME = "AAAAA";
    private static final String UPDATED_GODOWN_NAME = "BBBBB";
    private static final String DEFAULT_STORAGE_CAPACITY = "AAAAA";
    private static final String UPDATED_STORAGE_CAPACITY = "BBBBB";
    private static final String DEFAULT_ADDRESS_1 = "AAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBB";
    private static final String DEFAULT_ADDRESS_2 = "AAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBB";
    private static final String DEFAULT_CITY = "AAAAA";
    private static final String UPDATED_CITY = "BBBBB";
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";
    private static final String DEFAULT_ZIPCODE = "AAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBB";

    private static final Double DEFAULT_LONGITUDE_POS = 1D;
    private static final Double UPDATED_LONGITUDE_POS = 2D;

    private static final Double DEFAULT_LATITUDE_POS = 1D;
    private static final Double UPDATED_LATITUDE_POS = 2D;
    private static final String DEFAULT_COLOR_CODE = "AAAAA";
    private static final String UPDATED_COLOR_CODE = "BBBBB";

    @Inject
    private StockBufferNodeRepository stockBufferNodeRepository;

    @Inject
    private StockBufferNodeSearchRepository stockBufferNodeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStockBufferNodeMockMvc;

    private StockBufferNode stockBufferNode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StockBufferNodeResource stockBufferNodeResource = new StockBufferNodeResource();
        ReflectionTestUtils.setField(stockBufferNodeResource, "stockBufferNodeSearchRepository", stockBufferNodeSearchRepository);
        ReflectionTestUtils.setField(stockBufferNodeResource, "stockBufferNodeRepository", stockBufferNodeRepository);
        this.restStockBufferNodeMockMvc = MockMvcBuilders.standaloneSetup(stockBufferNodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        stockBufferNodeSearchRepository.deleteAll();
        stockBufferNode = new StockBufferNode();
        stockBufferNode.setGodownName(DEFAULT_GODOWN_NAME);
        stockBufferNode.setStorageCapacity(DEFAULT_STORAGE_CAPACITY);
        stockBufferNode.setAddress1(DEFAULT_ADDRESS_1);
        stockBufferNode.setAddress2(DEFAULT_ADDRESS_2);
        stockBufferNode.setCity(DEFAULT_CITY);
        stockBufferNode.setState(DEFAULT_STATE);
        stockBufferNode.setZipcode(DEFAULT_ZIPCODE);
        stockBufferNode.setLongitudePos(DEFAULT_LONGITUDE_POS);
        stockBufferNode.setLatitudePos(DEFAULT_LATITUDE_POS);
        stockBufferNode.setColorCode(DEFAULT_COLOR_CODE);
    }

    @Test
    @Transactional
    public void createStockBufferNode() throws Exception {
        int databaseSizeBeforeCreate = stockBufferNodeRepository.findAll().size();

        // Create the StockBufferNode

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isCreated());

        // Validate the StockBufferNode in the database
        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeCreate + 1);
        StockBufferNode testStockBufferNode = stockBufferNodes.get(stockBufferNodes.size() - 1);
        assertThat(testStockBufferNode.getGodownName()).isEqualTo(DEFAULT_GODOWN_NAME);
        assertThat(testStockBufferNode.getStorageCapacity()).isEqualTo(DEFAULT_STORAGE_CAPACITY);
        assertThat(testStockBufferNode.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testStockBufferNode.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testStockBufferNode.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testStockBufferNode.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testStockBufferNode.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testStockBufferNode.getLongitudePos()).isEqualTo(DEFAULT_LONGITUDE_POS);
        assertThat(testStockBufferNode.getLatitudePos()).isEqualTo(DEFAULT_LATITUDE_POS);
        assertThat(testStockBufferNode.getColorCode()).isEqualTo(DEFAULT_COLOR_CODE);

        // Validate the StockBufferNode in ElasticSearch
        StockBufferNode stockBufferNodeEs = stockBufferNodeSearchRepository.findOne(testStockBufferNode.getId());
        assertThat(stockBufferNodeEs).isEqualToComparingFieldByField(testStockBufferNode);
    }

    @Test
    @Transactional
    public void checkGodownNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setGodownName(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorageCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setStorageCapacity(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setAddress1(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddress2IsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setAddress2(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setCity(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setState(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setZipcode(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudePosIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setLongitudePos(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudePosIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setLatitudePos(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBufferNodeRepository.findAll().size();
        // set the field null
        stockBufferNode.setColorCode(null);

        // Create the StockBufferNode, which fails.

        restStockBufferNodeMockMvc.perform(post("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(stockBufferNode)))
                .andExpect(status().isBadRequest());

        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockBufferNodes() throws Exception {
        // Initialize the database
        stockBufferNodeRepository.saveAndFlush(stockBufferNode);

        // Get all the stockBufferNodes
        restStockBufferNodeMockMvc.perform(get("/api/stock-buffer-nodes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(stockBufferNode.getId().intValue())))
                .andExpect(jsonPath("$.[*].godownName").value(hasItem(DEFAULT_GODOWN_NAME.toString())))
                .andExpect(jsonPath("$.[*].storageCapacity").value(hasItem(DEFAULT_STORAGE_CAPACITY.toString())))
                .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
                .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
                .andExpect(jsonPath("$.[*].longitudePos").value(hasItem(DEFAULT_LONGITUDE_POS.doubleValue())))
                .andExpect(jsonPath("$.[*].latitudePos").value(hasItem(DEFAULT_LATITUDE_POS.doubleValue())))
                .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE.toString())));
    }

    @Test
    @Transactional
    public void getStockBufferNode() throws Exception {
        // Initialize the database
        stockBufferNodeRepository.saveAndFlush(stockBufferNode);

        // Get the stockBufferNode
        restStockBufferNodeMockMvc.perform(get("/api/stock-buffer-nodes/{id}", stockBufferNode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(stockBufferNode.getId().intValue()))
            .andExpect(jsonPath("$.godownName").value(DEFAULT_GODOWN_NAME.toString()))
            .andExpect(jsonPath("$.storageCapacity").value(DEFAULT_STORAGE_CAPACITY.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE.toString()))
            .andExpect(jsonPath("$.longitudePos").value(DEFAULT_LONGITUDE_POS.doubleValue()))
            .andExpect(jsonPath("$.latitudePos").value(DEFAULT_LATITUDE_POS.doubleValue()))
            .andExpect(jsonPath("$.colorCode").value(DEFAULT_COLOR_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockBufferNode() throws Exception {
        // Get the stockBufferNode
        restStockBufferNodeMockMvc.perform(get("/api/stock-buffer-nodes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockBufferNode() throws Exception {
        // Initialize the database
        stockBufferNodeRepository.saveAndFlush(stockBufferNode);
        stockBufferNodeSearchRepository.save(stockBufferNode);
        int databaseSizeBeforeUpdate = stockBufferNodeRepository.findAll().size();

        // Update the stockBufferNode
        StockBufferNode updatedStockBufferNode = new StockBufferNode();
        updatedStockBufferNode.setId(stockBufferNode.getId());
        updatedStockBufferNode.setGodownName(UPDATED_GODOWN_NAME);
        updatedStockBufferNode.setStorageCapacity(UPDATED_STORAGE_CAPACITY);
        updatedStockBufferNode.setAddress1(UPDATED_ADDRESS_1);
        updatedStockBufferNode.setAddress2(UPDATED_ADDRESS_2);
        updatedStockBufferNode.setCity(UPDATED_CITY);
        updatedStockBufferNode.setState(UPDATED_STATE);
        updatedStockBufferNode.setZipcode(UPDATED_ZIPCODE);
        updatedStockBufferNode.setLongitudePos(UPDATED_LONGITUDE_POS);
        updatedStockBufferNode.setLatitudePos(UPDATED_LATITUDE_POS);
        updatedStockBufferNode.setColorCode(UPDATED_COLOR_CODE);

        restStockBufferNodeMockMvc.perform(put("/api/stock-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStockBufferNode)))
                .andExpect(status().isOk());

        // Validate the StockBufferNode in the database
        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeUpdate);
        StockBufferNode testStockBufferNode = stockBufferNodes.get(stockBufferNodes.size() - 1);
        assertThat(testStockBufferNode.getGodownName()).isEqualTo(UPDATED_GODOWN_NAME);
        assertThat(testStockBufferNode.getStorageCapacity()).isEqualTo(UPDATED_STORAGE_CAPACITY);
        assertThat(testStockBufferNode.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testStockBufferNode.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testStockBufferNode.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testStockBufferNode.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testStockBufferNode.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testStockBufferNode.getLongitudePos()).isEqualTo(UPDATED_LONGITUDE_POS);
        assertThat(testStockBufferNode.getLatitudePos()).isEqualTo(UPDATED_LATITUDE_POS);
        assertThat(testStockBufferNode.getColorCode()).isEqualTo(UPDATED_COLOR_CODE);

        // Validate the StockBufferNode in ElasticSearch
        StockBufferNode stockBufferNodeEs = stockBufferNodeSearchRepository.findOne(testStockBufferNode.getId());
        assertThat(stockBufferNodeEs).isEqualToComparingFieldByField(testStockBufferNode);
    }

    @Test
    @Transactional
    public void deleteStockBufferNode() throws Exception {
        // Initialize the database
        stockBufferNodeRepository.saveAndFlush(stockBufferNode);
        stockBufferNodeSearchRepository.save(stockBufferNode);
        int databaseSizeBeforeDelete = stockBufferNodeRepository.findAll().size();

        // Get the stockBufferNode
        restStockBufferNodeMockMvc.perform(delete("/api/stock-buffer-nodes/{id}", stockBufferNode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean stockBufferNodeExistsInEs = stockBufferNodeSearchRepository.exists(stockBufferNode.getId());
        assertThat(stockBufferNodeExistsInEs).isFalse();

        // Validate the database is empty
        List<StockBufferNode> stockBufferNodes = stockBufferNodeRepository.findAll();
        assertThat(stockBufferNodes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStockBufferNode() throws Exception {
        // Initialize the database
        stockBufferNodeRepository.saveAndFlush(stockBufferNode);
        stockBufferNodeSearchRepository.save(stockBufferNode);

        // Search the stockBufferNode
        restStockBufferNodeMockMvc.perform(get("/api/_search/stock-buffer-nodes?query=id:" + stockBufferNode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockBufferNode.getId().intValue())))
            .andExpect(jsonPath("$.[*].godownName").value(hasItem(DEFAULT_GODOWN_NAME.toString())))
            .andExpect(jsonPath("$.[*].storageCapacity").value(hasItem(DEFAULT_STORAGE_CAPACITY.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
            .andExpect(jsonPath("$.[*].longitudePos").value(hasItem(DEFAULT_LONGITUDE_POS.doubleValue())))
            .andExpect(jsonPath("$.[*].latitudePos").value(hasItem(DEFAULT_LATITUDE_POS.doubleValue())))
            .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE.toString())));
    }
}
