package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.BillingNode;
import org.gubbilabs.ecbike.repository.BillingNodeRepository;
import org.gubbilabs.ecbike.repository.search.BillingNodeSearchRepository;

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
 * Test class for the BillingNodeResource REST controller.
 *
 * @see BillingNodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class BillingNodeResourceIntTest {

    private static final String DEFAULT_BILL_CENTER_NAME = "AAAAA";
    private static final String UPDATED_BILL_CENTER_NAME = "BBBBB";
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
    private BillingNodeRepository billingNodeRepository;

    @Inject
    private BillingNodeSearchRepository billingNodeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBillingNodeMockMvc;

    private BillingNode billingNode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BillingNodeResource billingNodeResource = new BillingNodeResource();
        ReflectionTestUtils.setField(billingNodeResource, "billingNodeSearchRepository", billingNodeSearchRepository);
        ReflectionTestUtils.setField(billingNodeResource, "billingNodeRepository", billingNodeRepository);
        this.restBillingNodeMockMvc = MockMvcBuilders.standaloneSetup(billingNodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        billingNodeSearchRepository.deleteAll();
        billingNode = new BillingNode();
        billingNode.setBillCenterName(DEFAULT_BILL_CENTER_NAME);
        billingNode.setStorageCapacity(DEFAULT_STORAGE_CAPACITY);
        billingNode.setAddress1(DEFAULT_ADDRESS_1);
        billingNode.setAddress2(DEFAULT_ADDRESS_2);
        billingNode.setCity(DEFAULT_CITY);
        billingNode.setState(DEFAULT_STATE);
        billingNode.setZipcode(DEFAULT_ZIPCODE);
        billingNode.setLongitudePos(DEFAULT_LONGITUDE_POS);
        billingNode.setLatitudePos(DEFAULT_LATITUDE_POS);
        billingNode.setColorCode(DEFAULT_COLOR_CODE);
    }

    @Test
    @Transactional
    public void createBillingNode() throws Exception {
        int databaseSizeBeforeCreate = billingNodeRepository.findAll().size();

        // Create the BillingNode

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isCreated());

        // Validate the BillingNode in the database
        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeCreate + 1);
        BillingNode testBillingNode = billingNodes.get(billingNodes.size() - 1);
        assertThat(testBillingNode.getBillCenterName()).isEqualTo(DEFAULT_BILL_CENTER_NAME);
        assertThat(testBillingNode.getStorageCapacity()).isEqualTo(DEFAULT_STORAGE_CAPACITY);
        assertThat(testBillingNode.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testBillingNode.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testBillingNode.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testBillingNode.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBillingNode.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testBillingNode.getLongitudePos()).isEqualTo(DEFAULT_LONGITUDE_POS);
        assertThat(testBillingNode.getLatitudePos()).isEqualTo(DEFAULT_LATITUDE_POS);
        assertThat(testBillingNode.getColorCode()).isEqualTo(DEFAULT_COLOR_CODE);

        // Validate the BillingNode in ElasticSearch
        BillingNode billingNodeEs = billingNodeSearchRepository.findOne(testBillingNode.getId());
        assertThat(billingNodeEs).isEqualToComparingFieldByField(testBillingNode);
    }

    @Test
    @Transactional
    public void checkBillCenterNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setBillCenterName(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStorageCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setStorageCapacity(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setAddress1(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddress2IsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setAddress2(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setCity(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setState(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setZipcode(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudePosIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setLongitudePos(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudePosIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setLatitudePos(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = billingNodeRepository.findAll().size();
        // set the field null
        billingNode.setColorCode(null);

        // Create the BillingNode, which fails.

        restBillingNodeMockMvc.perform(post("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billingNode)))
                .andExpect(status().isBadRequest());

        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBillingNodes() throws Exception {
        // Initialize the database
        billingNodeRepository.saveAndFlush(billingNode);

        // Get all the billingNodes
        restBillingNodeMockMvc.perform(get("/api/billing-nodes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(billingNode.getId().intValue())))
                .andExpect(jsonPath("$.[*].billCenterName").value(hasItem(DEFAULT_BILL_CENTER_NAME.toString())))
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
    public void getBillingNode() throws Exception {
        // Initialize the database
        billingNodeRepository.saveAndFlush(billingNode);

        // Get the billingNode
        restBillingNodeMockMvc.perform(get("/api/billing-nodes/{id}", billingNode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(billingNode.getId().intValue()))
            .andExpect(jsonPath("$.billCenterName").value(DEFAULT_BILL_CENTER_NAME.toString()))
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
    public void getNonExistingBillingNode() throws Exception {
        // Get the billingNode
        restBillingNodeMockMvc.perform(get("/api/billing-nodes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillingNode() throws Exception {
        // Initialize the database
        billingNodeRepository.saveAndFlush(billingNode);
        billingNodeSearchRepository.save(billingNode);
        int databaseSizeBeforeUpdate = billingNodeRepository.findAll().size();

        // Update the billingNode
        BillingNode updatedBillingNode = new BillingNode();
        updatedBillingNode.setId(billingNode.getId());
        updatedBillingNode.setBillCenterName(UPDATED_BILL_CENTER_NAME);
        updatedBillingNode.setStorageCapacity(UPDATED_STORAGE_CAPACITY);
        updatedBillingNode.setAddress1(UPDATED_ADDRESS_1);
        updatedBillingNode.setAddress2(UPDATED_ADDRESS_2);
        updatedBillingNode.setCity(UPDATED_CITY);
        updatedBillingNode.setState(UPDATED_STATE);
        updatedBillingNode.setZipcode(UPDATED_ZIPCODE);
        updatedBillingNode.setLongitudePos(UPDATED_LONGITUDE_POS);
        updatedBillingNode.setLatitudePos(UPDATED_LATITUDE_POS);
        updatedBillingNode.setColorCode(UPDATED_COLOR_CODE);

        restBillingNodeMockMvc.perform(put("/api/billing-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBillingNode)))
                .andExpect(status().isOk());

        // Validate the BillingNode in the database
        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeUpdate);
        BillingNode testBillingNode = billingNodes.get(billingNodes.size() - 1);
        assertThat(testBillingNode.getBillCenterName()).isEqualTo(UPDATED_BILL_CENTER_NAME);
        assertThat(testBillingNode.getStorageCapacity()).isEqualTo(UPDATED_STORAGE_CAPACITY);
        assertThat(testBillingNode.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testBillingNode.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testBillingNode.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testBillingNode.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBillingNode.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testBillingNode.getLongitudePos()).isEqualTo(UPDATED_LONGITUDE_POS);
        assertThat(testBillingNode.getLatitudePos()).isEqualTo(UPDATED_LATITUDE_POS);
        assertThat(testBillingNode.getColorCode()).isEqualTo(UPDATED_COLOR_CODE);

        // Validate the BillingNode in ElasticSearch
        BillingNode billingNodeEs = billingNodeSearchRepository.findOne(testBillingNode.getId());
        assertThat(billingNodeEs).isEqualToComparingFieldByField(testBillingNode);
    }

    @Test
    @Transactional
    public void deleteBillingNode() throws Exception {
        // Initialize the database
        billingNodeRepository.saveAndFlush(billingNode);
        billingNodeSearchRepository.save(billingNode);
        int databaseSizeBeforeDelete = billingNodeRepository.findAll().size();

        // Get the billingNode
        restBillingNodeMockMvc.perform(delete("/api/billing-nodes/{id}", billingNode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean billingNodeExistsInEs = billingNodeSearchRepository.exists(billingNode.getId());
        assertThat(billingNodeExistsInEs).isFalse();

        // Validate the database is empty
        List<BillingNode> billingNodes = billingNodeRepository.findAll();
        assertThat(billingNodes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBillingNode() throws Exception {
        // Initialize the database
        billingNodeRepository.saveAndFlush(billingNode);
        billingNodeSearchRepository.save(billingNode);

        // Search the billingNode
        restBillingNodeMockMvc.perform(get("/api/_search/billing-nodes?query=id:" + billingNode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billingNode.getId().intValue())))
            .andExpect(jsonPath("$.[*].billCenterName").value(hasItem(DEFAULT_BILL_CENTER_NAME.toString())))
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
