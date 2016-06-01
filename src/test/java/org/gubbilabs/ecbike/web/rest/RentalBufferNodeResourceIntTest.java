package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.RentalBufferNode;
import org.gubbilabs.ecbike.repository.RentalBufferNodeRepository;
import org.gubbilabs.ecbike.repository.search.RentalBufferNodeSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RentalBufferNodeResource REST controller.
 *
 * @see RentalBufferNodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class RentalBufferNodeResourceIntTest {

    private static final String DEFAULT_STATION_NAME = "AAAAA";
    private static final String UPDATED_STATION_NAME = "BBBBB";
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

    private static final byte[] DEFAULT_PHOTO_OF_LOCATION = TestUtil.createByteArray(1000, "0");
    private static final byte[] UPDATED_PHOTO_OF_LOCATION = TestUtil.createByteArray(35000, "1");
    private static final String DEFAULT_PHOTO_OF_LOCATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_OF_LOCATION_CONTENT_TYPE = "image/png";

    @Inject
    private RentalBufferNodeRepository rentalBufferNodeRepository;

    @Inject
    private RentalBufferNodeSearchRepository rentalBufferNodeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRentalBufferNodeMockMvc;

    private RentalBufferNode rentalBufferNode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RentalBufferNodeResource rentalBufferNodeResource = new RentalBufferNodeResource();
        ReflectionTestUtils.setField(rentalBufferNodeResource, "rentalBufferNodeSearchRepository", rentalBufferNodeSearchRepository);
        ReflectionTestUtils.setField(rentalBufferNodeResource, "rentalBufferNodeRepository", rentalBufferNodeRepository);
        this.restRentalBufferNodeMockMvc = MockMvcBuilders.standaloneSetup(rentalBufferNodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        rentalBufferNodeSearchRepository.deleteAll();
        rentalBufferNode = new RentalBufferNode();
        rentalBufferNode.setStationName(DEFAULT_STATION_NAME);
        rentalBufferNode.setAddress1(DEFAULT_ADDRESS_1);
        rentalBufferNode.setAddress2(DEFAULT_ADDRESS_2);
        rentalBufferNode.setCity(DEFAULT_CITY);
        rentalBufferNode.setState(DEFAULT_STATE);
        rentalBufferNode.setZipcode(DEFAULT_ZIPCODE);
        rentalBufferNode.setLongitudePos(DEFAULT_LONGITUDE_POS);
        rentalBufferNode.setLatitudePos(DEFAULT_LATITUDE_POS);
        rentalBufferNode.setColorCode(DEFAULT_COLOR_CODE);
        rentalBufferNode.setPhotoOfLocation(DEFAULT_PHOTO_OF_LOCATION);
        rentalBufferNode.setPhotoOfLocationContentType(DEFAULT_PHOTO_OF_LOCATION_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createRentalBufferNode() throws Exception {
        int databaseSizeBeforeCreate = rentalBufferNodeRepository.findAll().size();

        // Create the RentalBufferNode

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isCreated());

        // Validate the RentalBufferNode in the database
        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeCreate + 1);
        RentalBufferNode testRentalBufferNode = rentalBufferNodes.get(rentalBufferNodes.size() - 1);
        assertThat(testRentalBufferNode.getStationName()).isEqualTo(DEFAULT_STATION_NAME);
        assertThat(testRentalBufferNode.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testRentalBufferNode.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testRentalBufferNode.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testRentalBufferNode.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testRentalBufferNode.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testRentalBufferNode.getLongitudePos()).isEqualTo(DEFAULT_LONGITUDE_POS);
        assertThat(testRentalBufferNode.getLatitudePos()).isEqualTo(DEFAULT_LATITUDE_POS);
        assertThat(testRentalBufferNode.getColorCode()).isEqualTo(DEFAULT_COLOR_CODE);
        assertThat(testRentalBufferNode.getPhotoOfLocation()).isEqualTo(DEFAULT_PHOTO_OF_LOCATION);
        assertThat(testRentalBufferNode.getPhotoOfLocationContentType()).isEqualTo(DEFAULT_PHOTO_OF_LOCATION_CONTENT_TYPE);

        // Validate the RentalBufferNode in ElasticSearch
        RentalBufferNode rentalBufferNodeEs = rentalBufferNodeSearchRepository.findOne(testRentalBufferNode.getId());
        assertThat(rentalBufferNodeEs).isEqualToComparingFieldByField(testRentalBufferNode);
    }

    @Test
    @Transactional
    public void checkStationNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setStationName(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddress1IsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setAddress1(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddress2IsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setAddress2(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setCity(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setState(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setZipcode(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongitudePosIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setLongitudePos(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatitudePosIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setLatitudePos(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setColorCode(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhotoOfLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = rentalBufferNodeRepository.findAll().size();
        // set the field null
        rentalBufferNode.setPhotoOfLocation(null);

        // Create the RentalBufferNode, which fails.

        restRentalBufferNodeMockMvc.perform(post("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rentalBufferNode)))
                .andExpect(status().isBadRequest());

        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRentalBufferNodes() throws Exception {
        // Initialize the database
        rentalBufferNodeRepository.saveAndFlush(rentalBufferNode);

        // Get all the rentalBufferNodes
        restRentalBufferNodeMockMvc.perform(get("/api/rental-buffer-nodes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rentalBufferNode.getId().intValue())))
                .andExpect(jsonPath("$.[*].stationName").value(hasItem(DEFAULT_STATION_NAME.toString())))
                .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
                .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
                .andExpect(jsonPath("$.[*].longitudePos").value(hasItem(DEFAULT_LONGITUDE_POS.doubleValue())))
                .andExpect(jsonPath("$.[*].latitudePos").value(hasItem(DEFAULT_LATITUDE_POS.doubleValue())))
                .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE.toString())))
                .andExpect(jsonPath("$.[*].photoOfLocationContentType").value(hasItem(DEFAULT_PHOTO_OF_LOCATION_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photoOfLocation").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_OF_LOCATION))));
    }

    @Test
    @Transactional
    public void getRentalBufferNode() throws Exception {
        // Initialize the database
        rentalBufferNodeRepository.saveAndFlush(rentalBufferNode);

        // Get the rentalBufferNode
        restRentalBufferNodeMockMvc.perform(get("/api/rental-buffer-nodes/{id}", rentalBufferNode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(rentalBufferNode.getId().intValue()))
            .andExpect(jsonPath("$.stationName").value(DEFAULT_STATION_NAME.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE.toString()))
            .andExpect(jsonPath("$.longitudePos").value(DEFAULT_LONGITUDE_POS.doubleValue()))
            .andExpect(jsonPath("$.latitudePos").value(DEFAULT_LATITUDE_POS.doubleValue()))
            .andExpect(jsonPath("$.colorCode").value(DEFAULT_COLOR_CODE.toString()))
            .andExpect(jsonPath("$.photoOfLocationContentType").value(DEFAULT_PHOTO_OF_LOCATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoOfLocation").value(Base64Utils.encodeToString(DEFAULT_PHOTO_OF_LOCATION)));
    }

    @Test
    @Transactional
    public void getNonExistingRentalBufferNode() throws Exception {
        // Get the rentalBufferNode
        restRentalBufferNodeMockMvc.perform(get("/api/rental-buffer-nodes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRentalBufferNode() throws Exception {
        // Initialize the database
        rentalBufferNodeRepository.saveAndFlush(rentalBufferNode);
        rentalBufferNodeSearchRepository.save(rentalBufferNode);
        int databaseSizeBeforeUpdate = rentalBufferNodeRepository.findAll().size();

        // Update the rentalBufferNode
        RentalBufferNode updatedRentalBufferNode = new RentalBufferNode();
        updatedRentalBufferNode.setId(rentalBufferNode.getId());
        updatedRentalBufferNode.setStationName(UPDATED_STATION_NAME);
        updatedRentalBufferNode.setAddress1(UPDATED_ADDRESS_1);
        updatedRentalBufferNode.setAddress2(UPDATED_ADDRESS_2);
        updatedRentalBufferNode.setCity(UPDATED_CITY);
        updatedRentalBufferNode.setState(UPDATED_STATE);
        updatedRentalBufferNode.setZipcode(UPDATED_ZIPCODE);
        updatedRentalBufferNode.setLongitudePos(UPDATED_LONGITUDE_POS);
        updatedRentalBufferNode.setLatitudePos(UPDATED_LATITUDE_POS);
        updatedRentalBufferNode.setColorCode(UPDATED_COLOR_CODE);
        updatedRentalBufferNode.setPhotoOfLocation(UPDATED_PHOTO_OF_LOCATION);
        updatedRentalBufferNode.setPhotoOfLocationContentType(UPDATED_PHOTO_OF_LOCATION_CONTENT_TYPE);

        restRentalBufferNodeMockMvc.perform(put("/api/rental-buffer-nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRentalBufferNode)))
                .andExpect(status().isOk());

        // Validate the RentalBufferNode in the database
        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeUpdate);
        RentalBufferNode testRentalBufferNode = rentalBufferNodes.get(rentalBufferNodes.size() - 1);
        assertThat(testRentalBufferNode.getStationName()).isEqualTo(UPDATED_STATION_NAME);
        assertThat(testRentalBufferNode.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testRentalBufferNode.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testRentalBufferNode.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testRentalBufferNode.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testRentalBufferNode.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testRentalBufferNode.getLongitudePos()).isEqualTo(UPDATED_LONGITUDE_POS);
        assertThat(testRentalBufferNode.getLatitudePos()).isEqualTo(UPDATED_LATITUDE_POS);
        assertThat(testRentalBufferNode.getColorCode()).isEqualTo(UPDATED_COLOR_CODE);
        assertThat(testRentalBufferNode.getPhotoOfLocation()).isEqualTo(UPDATED_PHOTO_OF_LOCATION);
        assertThat(testRentalBufferNode.getPhotoOfLocationContentType()).isEqualTo(UPDATED_PHOTO_OF_LOCATION_CONTENT_TYPE);

        // Validate the RentalBufferNode in ElasticSearch
        RentalBufferNode rentalBufferNodeEs = rentalBufferNodeSearchRepository.findOne(testRentalBufferNode.getId());
        assertThat(rentalBufferNodeEs).isEqualToComparingFieldByField(testRentalBufferNode);
    }

    @Test
    @Transactional
    public void deleteRentalBufferNode() throws Exception {
        // Initialize the database
        rentalBufferNodeRepository.saveAndFlush(rentalBufferNode);
        rentalBufferNodeSearchRepository.save(rentalBufferNode);
        int databaseSizeBeforeDelete = rentalBufferNodeRepository.findAll().size();

        // Get the rentalBufferNode
        restRentalBufferNodeMockMvc.perform(delete("/api/rental-buffer-nodes/{id}", rentalBufferNode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean rentalBufferNodeExistsInEs = rentalBufferNodeSearchRepository.exists(rentalBufferNode.getId());
        assertThat(rentalBufferNodeExistsInEs).isFalse();

        // Validate the database is empty
        List<RentalBufferNode> rentalBufferNodes = rentalBufferNodeRepository.findAll();
        assertThat(rentalBufferNodes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRentalBufferNode() throws Exception {
        // Initialize the database
        rentalBufferNodeRepository.saveAndFlush(rentalBufferNode);
        rentalBufferNodeSearchRepository.save(rentalBufferNode);

        // Search the rentalBufferNode
        restRentalBufferNodeMockMvc.perform(get("/api/_search/rental-buffer-nodes?query=id:" + rentalBufferNode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rentalBufferNode.getId().intValue())))
            .andExpect(jsonPath("$.[*].stationName").value(hasItem(DEFAULT_STATION_NAME.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
            .andExpect(jsonPath("$.[*].longitudePos").value(hasItem(DEFAULT_LONGITUDE_POS.doubleValue())))
            .andExpect(jsonPath("$.[*].latitudePos").value(hasItem(DEFAULT_LATITUDE_POS.doubleValue())))
            .andExpect(jsonPath("$.[*].colorCode").value(hasItem(DEFAULT_COLOR_CODE.toString())))
            .andExpect(jsonPath("$.[*].photoOfLocationContentType").value(hasItem(DEFAULT_PHOTO_OF_LOCATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoOfLocation").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_OF_LOCATION))));
    }
}
