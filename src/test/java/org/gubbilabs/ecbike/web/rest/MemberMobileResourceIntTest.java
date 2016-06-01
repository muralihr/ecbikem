package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.MemberMobile;
import org.gubbilabs.ecbike.repository.MemberMobileRepository;
import org.gubbilabs.ecbike.repository.search.MemberMobileSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MemberMobileResource REST controller.
 *
 * @see MemberMobileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class MemberMobileResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_EMAIL_ID = "AAAAA";
    private static final String UPDATED_EMAIL_ID = "BBBBB";
    private static final String DEFAULT_MOBILE_NO = "AAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBB";
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
    private static final String DEFAULT_COUNTRY = "AAAAA";
    private static final String UPDATED_COUNTRY = "BBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_PHOTO_ID_PROOF = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_ID_PROOF = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_ID_PROOF_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_ID_PROOF_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_MY_CURRENT_RENT_UNITS = 1;
    private static final Integer UPDATED_MY_CURRENT_RENT_UNITS = 2;

    private static final Integer DEFAULT_MY_CHARGED_RENT_UNITS = 1;
    private static final Integer UPDATED_MY_CHARGED_RENT_UNITS = 2;

    private static final Integer DEFAULT_BEHAVIOR_STATUS = 1;
    private static final Integer UPDATED_BEHAVIOR_STATUS = 2;

    private static final Integer DEFAULT_MY_CURRENT_FINE_CHARGES = 1;
    private static final Integer UPDATED_MY_CURRENT_FINE_CHARGES = 2;

    private static final LocalDate DEFAULT_DATE_OF_EXPIRATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_EXPIRATION = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_USER_NAME = "AAAAA";
    private static final String UPDATED_USER_NAME = "BBBBB";
    private static final String DEFAULT_PASS_WORD = "AAAAA";
    private static final String UPDATED_PASS_WORD = "BBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    @Inject
    private MemberMobileRepository memberMobileRepository;

    @Inject
    private MemberMobileSearchRepository memberMobileSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberMobileMockMvc;

    private MemberMobile memberMobile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberMobileResource memberMobileResource = new MemberMobileResource();
        ReflectionTestUtils.setField(memberMobileResource, "memberMobileSearchRepository", memberMobileSearchRepository);
        ReflectionTestUtils.setField(memberMobileResource, "memberMobileRepository", memberMobileRepository);
        this.restMemberMobileMockMvc = MockMvcBuilders.standaloneSetup(memberMobileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberMobileSearchRepository.deleteAll();
        memberMobile = new MemberMobile();
        memberMobile.setFirstName(DEFAULT_FIRST_NAME);
        memberMobile.setLastName(DEFAULT_LAST_NAME);
        memberMobile.setDateOfBirth(DEFAULT_DATE_OF_BIRTH);
        memberMobile.setEmailId(DEFAULT_EMAIL_ID);
        memberMobile.setMobileNo(DEFAULT_MOBILE_NO);
        memberMobile.setAddress1(DEFAULT_ADDRESS_1);
        memberMobile.setAddress2(DEFAULT_ADDRESS_2);
        memberMobile.setCity(DEFAULT_CITY);
        memberMobile.setState(DEFAULT_STATE);
        memberMobile.setZipcode(DEFAULT_ZIPCODE);
        memberMobile.setCountry(DEFAULT_COUNTRY);
        memberMobile.setPhoto(DEFAULT_PHOTO);
        memberMobile.setPhotoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        memberMobile.setPhotoIDProof(DEFAULT_PHOTO_ID_PROOF);
        memberMobile.setPhotoIDProofContentType(DEFAULT_PHOTO_ID_PROOF_CONTENT_TYPE);
        memberMobile.setMyCurrentRentUnits(DEFAULT_MY_CURRENT_RENT_UNITS);
        memberMobile.setMyChargedRentUnits(DEFAULT_MY_CHARGED_RENT_UNITS);
        memberMobile.setBehaviorStatus(DEFAULT_BEHAVIOR_STATUS);
        memberMobile.setMyCurrentFineCharges(DEFAULT_MY_CURRENT_FINE_CHARGES);
        memberMobile.setDateOfExpiration(DEFAULT_DATE_OF_EXPIRATION);
        memberMobile.setUserName(DEFAULT_USER_NAME);
        memberMobile.setPassWord(DEFAULT_PASS_WORD);
        memberMobile.setActivated(DEFAULT_ACTIVATED);
    }

    @Test
    @Transactional
    public void createMemberMobile() throws Exception {
        int databaseSizeBeforeCreate = memberMobileRepository.findAll().size();

        // Create the MemberMobile

        restMemberMobileMockMvc.perform(post("/api/member-mobiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMobile)))
                .andExpect(status().isCreated());

        // Validate the MemberMobile in the database
        List<MemberMobile> memberMobiles = memberMobileRepository.findAll();
        assertThat(memberMobiles).hasSize(databaseSizeBeforeCreate + 1);
        MemberMobile testMemberMobile = memberMobiles.get(memberMobiles.size() - 1);
        assertThat(testMemberMobile.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMemberMobile.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMemberMobile.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testMemberMobile.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testMemberMobile.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testMemberMobile.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testMemberMobile.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testMemberMobile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMemberMobile.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testMemberMobile.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testMemberMobile.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testMemberMobile.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testMemberMobile.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testMemberMobile.getPhotoIDProof()).isEqualTo(DEFAULT_PHOTO_ID_PROOF);
        assertThat(testMemberMobile.getPhotoIDProofContentType()).isEqualTo(DEFAULT_PHOTO_ID_PROOF_CONTENT_TYPE);
        assertThat(testMemberMobile.getMyCurrentRentUnits()).isEqualTo(DEFAULT_MY_CURRENT_RENT_UNITS);
        assertThat(testMemberMobile.getMyChargedRentUnits()).isEqualTo(DEFAULT_MY_CHARGED_RENT_UNITS);
        assertThat(testMemberMobile.getBehaviorStatus()).isEqualTo(DEFAULT_BEHAVIOR_STATUS);
        assertThat(testMemberMobile.getMyCurrentFineCharges()).isEqualTo(DEFAULT_MY_CURRENT_FINE_CHARGES);
        assertThat(testMemberMobile.getDateOfExpiration()).isEqualTo(DEFAULT_DATE_OF_EXPIRATION);
        assertThat(testMemberMobile.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testMemberMobile.getPassWord()).isEqualTo(DEFAULT_PASS_WORD);
        assertThat(testMemberMobile.isActivated()).isEqualTo(DEFAULT_ACTIVATED);

        // Validate the MemberMobile in ElasticSearch
        MemberMobile memberMobileEs = memberMobileSearchRepository.findOne(testMemberMobile.getId());
        assertThat(memberMobileEs).isEqualToComparingFieldByField(testMemberMobile);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberMobileRepository.findAll().size();
        // set the field null
        memberMobile.setFirstName(null);

        // Create the MemberMobile, which fails.

        restMemberMobileMockMvc.perform(post("/api/member-mobiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMobile)))
                .andExpect(status().isBadRequest());

        List<MemberMobile> memberMobiles = memberMobileRepository.findAll();
        assertThat(memberMobiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberMobileRepository.findAll().size();
        // set the field null
        memberMobile.setDateOfBirth(null);

        // Create the MemberMobile, which fails.

        restMemberMobileMockMvc.perform(post("/api/member-mobiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMobile)))
                .andExpect(status().isBadRequest());

        List<MemberMobile> memberMobiles = memberMobileRepository.findAll();
        assertThat(memberMobiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberMobileRepository.findAll().size();
        // set the field null
        memberMobile.setZipcode(null);

        // Create the MemberMobile, which fails.

        restMemberMobileMockMvc.perform(post("/api/member-mobiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberMobile)))
                .andExpect(status().isBadRequest());

        List<MemberMobile> memberMobiles = memberMobileRepository.findAll();
        assertThat(memberMobiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberMobiles() throws Exception {
        // Initialize the database
        memberMobileRepository.saveAndFlush(memberMobile);

        // Get all the memberMobiles
        restMemberMobileMockMvc.perform(get("/api/member-mobiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(memberMobile.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
                .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID.toString())))
                .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.toString())))
                .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
                .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
                .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
                .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
                .andExpect(jsonPath("$.[*].photoIDProofContentType").value(hasItem(DEFAULT_PHOTO_ID_PROOF_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photoIDProof").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_ID_PROOF))))
                .andExpect(jsonPath("$.[*].myCurrentRentUnits").value(hasItem(DEFAULT_MY_CURRENT_RENT_UNITS)))
                .andExpect(jsonPath("$.[*].myChargedRentUnits").value(hasItem(DEFAULT_MY_CHARGED_RENT_UNITS)))
                .andExpect(jsonPath("$.[*].behaviorStatus").value(hasItem(DEFAULT_BEHAVIOR_STATUS)))
                .andExpect(jsonPath("$.[*].myCurrentFineCharges").value(hasItem(DEFAULT_MY_CURRENT_FINE_CHARGES)))
                .andExpect(jsonPath("$.[*].dateOfExpiration").value(hasItem(DEFAULT_DATE_OF_EXPIRATION.toString())))
                .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
                .andExpect(jsonPath("$.[*].passWord").value(hasItem(DEFAULT_PASS_WORD.toString())))
                .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }

    @Test
    @Transactional
    public void getMemberMobile() throws Exception {
        // Initialize the database
        memberMobileRepository.saveAndFlush(memberMobile);

        // Get the memberMobile
        restMemberMobileMockMvc.perform(get("/api/member-mobiles/{id}", memberMobile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(memberMobile.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID.toString()))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.photoIDProofContentType").value(DEFAULT_PHOTO_ID_PROOF_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoIDProof").value(Base64Utils.encodeToString(DEFAULT_PHOTO_ID_PROOF)))
            .andExpect(jsonPath("$.myCurrentRentUnits").value(DEFAULT_MY_CURRENT_RENT_UNITS))
            .andExpect(jsonPath("$.myChargedRentUnits").value(DEFAULT_MY_CHARGED_RENT_UNITS))
            .andExpect(jsonPath("$.behaviorStatus").value(DEFAULT_BEHAVIOR_STATUS))
            .andExpect(jsonPath("$.myCurrentFineCharges").value(DEFAULT_MY_CURRENT_FINE_CHARGES))
            .andExpect(jsonPath("$.dateOfExpiration").value(DEFAULT_DATE_OF_EXPIRATION.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.passWord").value(DEFAULT_PASS_WORD.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMemberMobile() throws Exception {
        // Get the memberMobile
        restMemberMobileMockMvc.perform(get("/api/member-mobiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberMobile() throws Exception {
        // Initialize the database
        memberMobileRepository.saveAndFlush(memberMobile);
        memberMobileSearchRepository.save(memberMobile);
        int databaseSizeBeforeUpdate = memberMobileRepository.findAll().size();

        // Update the memberMobile
        MemberMobile updatedMemberMobile = new MemberMobile();
        updatedMemberMobile.setId(memberMobile.getId());
        updatedMemberMobile.setFirstName(UPDATED_FIRST_NAME);
        updatedMemberMobile.setLastName(UPDATED_LAST_NAME);
        updatedMemberMobile.setDateOfBirth(UPDATED_DATE_OF_BIRTH);
        updatedMemberMobile.setEmailId(UPDATED_EMAIL_ID);
        updatedMemberMobile.setMobileNo(UPDATED_MOBILE_NO);
        updatedMemberMobile.setAddress1(UPDATED_ADDRESS_1);
        updatedMemberMobile.setAddress2(UPDATED_ADDRESS_2);
        updatedMemberMobile.setCity(UPDATED_CITY);
        updatedMemberMobile.setState(UPDATED_STATE);
        updatedMemberMobile.setZipcode(UPDATED_ZIPCODE);
        updatedMemberMobile.setCountry(UPDATED_COUNTRY);
        updatedMemberMobile.setPhoto(UPDATED_PHOTO);
        updatedMemberMobile.setPhotoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        updatedMemberMobile.setPhotoIDProof(UPDATED_PHOTO_ID_PROOF);
        updatedMemberMobile.setPhotoIDProofContentType(UPDATED_PHOTO_ID_PROOF_CONTENT_TYPE);
        updatedMemberMobile.setMyCurrentRentUnits(UPDATED_MY_CURRENT_RENT_UNITS);
        updatedMemberMobile.setMyChargedRentUnits(UPDATED_MY_CHARGED_RENT_UNITS);
        updatedMemberMobile.setBehaviorStatus(UPDATED_BEHAVIOR_STATUS);
        updatedMemberMobile.setMyCurrentFineCharges(UPDATED_MY_CURRENT_FINE_CHARGES);
        updatedMemberMobile.setDateOfExpiration(UPDATED_DATE_OF_EXPIRATION);
        updatedMemberMobile.setUserName(UPDATED_USER_NAME);
        updatedMemberMobile.setPassWord(UPDATED_PASS_WORD);
        updatedMemberMobile.setActivated(UPDATED_ACTIVATED);

        restMemberMobileMockMvc.perform(put("/api/member-mobiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMemberMobile)))
                .andExpect(status().isOk());

        // Validate the MemberMobile in the database
        List<MemberMobile> memberMobiles = memberMobileRepository.findAll();
        assertThat(memberMobiles).hasSize(databaseSizeBeforeUpdate);
        MemberMobile testMemberMobile = memberMobiles.get(memberMobiles.size() - 1);
        assertThat(testMemberMobile.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMemberMobile.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMemberMobile.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testMemberMobile.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testMemberMobile.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testMemberMobile.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testMemberMobile.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testMemberMobile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMemberMobile.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testMemberMobile.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testMemberMobile.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testMemberMobile.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testMemberMobile.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testMemberMobile.getPhotoIDProof()).isEqualTo(UPDATED_PHOTO_ID_PROOF);
        assertThat(testMemberMobile.getPhotoIDProofContentType()).isEqualTo(UPDATED_PHOTO_ID_PROOF_CONTENT_TYPE);
        assertThat(testMemberMobile.getMyCurrentRentUnits()).isEqualTo(UPDATED_MY_CURRENT_RENT_UNITS);
        assertThat(testMemberMobile.getMyChargedRentUnits()).isEqualTo(UPDATED_MY_CHARGED_RENT_UNITS);
        assertThat(testMemberMobile.getBehaviorStatus()).isEqualTo(UPDATED_BEHAVIOR_STATUS);
        assertThat(testMemberMobile.getMyCurrentFineCharges()).isEqualTo(UPDATED_MY_CURRENT_FINE_CHARGES);
        assertThat(testMemberMobile.getDateOfExpiration()).isEqualTo(UPDATED_DATE_OF_EXPIRATION);
        assertThat(testMemberMobile.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testMemberMobile.getPassWord()).isEqualTo(UPDATED_PASS_WORD);
        assertThat(testMemberMobile.isActivated()).isEqualTo(UPDATED_ACTIVATED);

        // Validate the MemberMobile in ElasticSearch
        MemberMobile memberMobileEs = memberMobileSearchRepository.findOne(testMemberMobile.getId());
        assertThat(memberMobileEs).isEqualToComparingFieldByField(testMemberMobile);
    }

    @Test
    @Transactional
    public void deleteMemberMobile() throws Exception {
        // Initialize the database
        memberMobileRepository.saveAndFlush(memberMobile);
        memberMobileSearchRepository.save(memberMobile);
        int databaseSizeBeforeDelete = memberMobileRepository.findAll().size();

        // Get the memberMobile
        restMemberMobileMockMvc.perform(delete("/api/member-mobiles/{id}", memberMobile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean memberMobileExistsInEs = memberMobileSearchRepository.exists(memberMobile.getId());
        assertThat(memberMobileExistsInEs).isFalse();

        // Validate the database is empty
        List<MemberMobile> memberMobiles = memberMobileRepository.findAll();
        assertThat(memberMobiles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMemberMobile() throws Exception {
        // Initialize the database
        memberMobileRepository.saveAndFlush(memberMobile);
        memberMobileSearchRepository.save(memberMobile);

        // Search the memberMobile
        restMemberMobileMockMvc.perform(get("/api/_search/member-mobiles?query=id:" + memberMobile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberMobile.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID.toString())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].photoIDProofContentType").value(hasItem(DEFAULT_PHOTO_ID_PROOF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoIDProof").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_ID_PROOF))))
            .andExpect(jsonPath("$.[*].myCurrentRentUnits").value(hasItem(DEFAULT_MY_CURRENT_RENT_UNITS)))
            .andExpect(jsonPath("$.[*].myChargedRentUnits").value(hasItem(DEFAULT_MY_CHARGED_RENT_UNITS)))
            .andExpect(jsonPath("$.[*].behaviorStatus").value(hasItem(DEFAULT_BEHAVIOR_STATUS)))
            .andExpect(jsonPath("$.[*].myCurrentFineCharges").value(hasItem(DEFAULT_MY_CURRENT_FINE_CHARGES)))
            .andExpect(jsonPath("$.[*].dateOfExpiration").value(hasItem(DEFAULT_DATE_OF_EXPIRATION.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].passWord").value(hasItem(DEFAULT_PASS_WORD.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())));
    }
}
