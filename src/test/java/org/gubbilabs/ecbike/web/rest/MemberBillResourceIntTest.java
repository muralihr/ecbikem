package org.gubbilabs.ecbike.web.rest;

import org.gubbilabs.ecbike.EcbikeApp;
import org.gubbilabs.ecbike.domain.MemberBill;
import org.gubbilabs.ecbike.repository.MemberBillRepository;
import org.gubbilabs.ecbike.repository.search.MemberBillSearchRepository;

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
 * Test class for the MemberBillResource REST controller.
 *
 * @see MemberBillResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EcbikeApp.class)
@WebAppConfiguration
@IntegrationTest
public class MemberBillResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE_OF_PAYMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_OF_PAYMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_OF_PAYMENT_STR = dateTimeFormatter.format(DEFAULT_DATE_OF_PAYMENT);

    @Inject
    private MemberBillRepository memberBillRepository;

    @Inject
    private MemberBillSearchRepository memberBillSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMemberBillMockMvc;

    private MemberBill memberBill;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MemberBillResource memberBillResource = new MemberBillResource();
        ReflectionTestUtils.setField(memberBillResource, "memberBillSearchRepository", memberBillSearchRepository);
        ReflectionTestUtils.setField(memberBillResource, "memberBillRepository", memberBillRepository);
        this.restMemberBillMockMvc = MockMvcBuilders.standaloneSetup(memberBillResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        memberBillSearchRepository.deleteAll();
        memberBill = new MemberBill();
        memberBill.setDateOfPayment(DEFAULT_DATE_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void createMemberBill() throws Exception {
        int databaseSizeBeforeCreate = memberBillRepository.findAll().size();

        // Create the MemberBill

        restMemberBillMockMvc.perform(post("/api/member-bills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberBill)))
                .andExpect(status().isCreated());

        // Validate the MemberBill in the database
        List<MemberBill> memberBills = memberBillRepository.findAll();
        assertThat(memberBills).hasSize(databaseSizeBeforeCreate + 1);
        MemberBill testMemberBill = memberBills.get(memberBills.size() - 1);
        assertThat(testMemberBill.getDateOfPayment()).isEqualTo(DEFAULT_DATE_OF_PAYMENT);

        // Validate the MemberBill in ElasticSearch
        MemberBill memberBillEs = memberBillSearchRepository.findOne(testMemberBill.getId());
        assertThat(memberBillEs).isEqualToComparingFieldByField(testMemberBill);
    }

    @Test
    @Transactional
    public void checkDateOfPaymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberBillRepository.findAll().size();
        // set the field null
        memberBill.setDateOfPayment(null);

        // Create the MemberBill, which fails.

        restMemberBillMockMvc.perform(post("/api/member-bills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(memberBill)))
                .andExpect(status().isBadRequest());

        List<MemberBill> memberBills = memberBillRepository.findAll();
        assertThat(memberBills).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMemberBills() throws Exception {
        // Initialize the database
        memberBillRepository.saveAndFlush(memberBill);

        // Get all the memberBills
        restMemberBillMockMvc.perform(get("/api/member-bills?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(memberBill.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateOfPayment").value(hasItem(DEFAULT_DATE_OF_PAYMENT_STR)));
    }

    @Test
    @Transactional
    public void getMemberBill() throws Exception {
        // Initialize the database
        memberBillRepository.saveAndFlush(memberBill);

        // Get the memberBill
        restMemberBillMockMvc.perform(get("/api/member-bills/{id}", memberBill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(memberBill.getId().intValue()))
            .andExpect(jsonPath("$.dateOfPayment").value(DEFAULT_DATE_OF_PAYMENT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingMemberBill() throws Exception {
        // Get the memberBill
        restMemberBillMockMvc.perform(get("/api/member-bills/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberBill() throws Exception {
        // Initialize the database
        memberBillRepository.saveAndFlush(memberBill);
        memberBillSearchRepository.save(memberBill);
        int databaseSizeBeforeUpdate = memberBillRepository.findAll().size();

        // Update the memberBill
        MemberBill updatedMemberBill = new MemberBill();
        updatedMemberBill.setId(memberBill.getId());
        updatedMemberBill.setDateOfPayment(UPDATED_DATE_OF_PAYMENT);

        restMemberBillMockMvc.perform(put("/api/member-bills")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMemberBill)))
                .andExpect(status().isOk());

        // Validate the MemberBill in the database
        List<MemberBill> memberBills = memberBillRepository.findAll();
        assertThat(memberBills).hasSize(databaseSizeBeforeUpdate);
        MemberBill testMemberBill = memberBills.get(memberBills.size() - 1);
        assertThat(testMemberBill.getDateOfPayment()).isEqualTo(UPDATED_DATE_OF_PAYMENT);

        // Validate the MemberBill in ElasticSearch
        MemberBill memberBillEs = memberBillSearchRepository.findOne(testMemberBill.getId());
        assertThat(memberBillEs).isEqualToComparingFieldByField(testMemberBill);
    }

    @Test
    @Transactional
    public void deleteMemberBill() throws Exception {
        // Initialize the database
        memberBillRepository.saveAndFlush(memberBill);
        memberBillSearchRepository.save(memberBill);
        int databaseSizeBeforeDelete = memberBillRepository.findAll().size();

        // Get the memberBill
        restMemberBillMockMvc.perform(delete("/api/member-bills/{id}", memberBill.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean memberBillExistsInEs = memberBillSearchRepository.exists(memberBill.getId());
        assertThat(memberBillExistsInEs).isFalse();

        // Validate the database is empty
        List<MemberBill> memberBills = memberBillRepository.findAll();
        assertThat(memberBills).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMemberBill() throws Exception {
        // Initialize the database
        memberBillRepository.saveAndFlush(memberBill);
        memberBillSearchRepository.save(memberBill);

        // Search the memberBill
        restMemberBillMockMvc.perform(get("/api/_search/member-bills?query=id:" + memberBill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberBill.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfPayment").value(hasItem(DEFAULT_DATE_OF_PAYMENT_STR)));
    }
}
