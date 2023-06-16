package com.ekta.telecom.RewardsCalculator.controllerTests;

import com.ekta.telecom.RewardsCalculator.exception.ApiErrorResponse;
import com.ekta.telecom.RewardsCalculator.exception.ErrorMessage;
import com.ekta.telecom.RewardsCalculator.model.RewardRS;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import com.ekta.telecom.RewardsCalculator.util.testUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations =
        {"classpath:application-test.properties"})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionControllerTests {

    private static final String TRANSACTION_API = "/manage/transaction";
    private static final String REWARDS_API = "/manage/rewards";
    private static final String CLIENTID = "CLIENT1";
    private static final String NON_EXISTING_CLIENTID = "CLIENT2";
    private static final long NON_EXISTING_TRANSACTIONID = 75;
    private final ObjectMapper OBJECT_MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    List<Transaction> multipleTransactions = new ArrayList<>();
    List<Transaction> singleTransaction = new ArrayList<>();
    @Autowired
    private MockMvc mocTransactionController;

    @BeforeEach
    void setUp() {
        multipleTransactions = testUtility.getMultipleTransactions();
        singleTransaction = testUtility.getSingleTransaction();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void addTransactionsTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(multipleTransactions);
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        TransactionRQRS transactionRQRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionRQRS.class);
        assertThat(transactionRQRS.getTransactions().get(0).getClientId()).isEqualTo(CLIENTID);
        assertThat(transactionRQRS.getTransactions()).hasSize(4);
        assertThat(transactionRQRS.getTransactions().get(0)).isEqualTo(multipleTransactions.get(0));
        assertThat(transactionRQRS.getTransactions().get(1)).isEqualTo(multipleTransactions.get(1));
        assertThat(transactionRQRS.getTransactions().get(2)).isEqualTo(multipleTransactions.get(2));
        assertThat(transactionRQRS.getTransactions().get(3)).isEqualTo(multipleTransactions.get(3));
    }

    @Test
    @Order(2)
    void getTransactionTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(TRANSACTION_API + "/1"))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        TransactionRQRS transactionRQRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionRQRS.class);
        assertThat(transactionRQRS.getTransactions()).hasSize(1);
        assertThat(transactionRQRS.getTransactions().get(0)).isEqualTo(multipleTransactions.get(0));
    }

    @Test
    @Order(3)
    void modifyTransactionTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .patch(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        TransactionRQRS transactionRQRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionRQRS.class);
        assertThat(transactionRQRS.getTransactions().get(0).getClientId()).isEqualTo(CLIENTID);
        assertThat(transactionRQRS.getTransactions()).hasSize(1);
        assertThat(transactionRQRS.getTransactions().get(0)).isEqualTo(singleTransaction.get(0));
        assertThat(transactionRQRS.getTransactions().get(0)).isNotEqualTo(multipleTransactions.get(0));
    }

    @Test
    @Order(4)
    void removeTransactionTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .delete(TRANSACTION_API + "/1"))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        TransactionRQRS transactionRQRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), TransactionRQRS.class);
        assertThat(transactionRQRS.getTransactions()).hasSize(1);
        assertThat(transactionRQRS.getTransactions().get(0)).isEqualTo(singleTransaction.get(0));
    }

    @Test
    @Order(5)
    void calculateRewardsTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(REWARDS_API + "/" + CLIENTID))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        RewardRS rewardRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), RewardRS.class);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENTID);
        assertThat(rewardRS.getTotalRewardAmount()).isEqualTo(150);
        assertThat(rewardRS.getMonthlyRewardAmount()).hasSize(2);
        assertThat(rewardRS.getMonthlyRewardAmount())
                .containsEntry("June", 100L)
                .containsEntry("May", 50L);
    }

    @Test
    void modifyNonExistingTransaction() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setId(NON_EXISTING_TRANSACTIONID);
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .patch(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(404);
    }

    @Test
    void getNonExistingTransactionTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(TRANSACTION_API + "/" + NON_EXISTING_TRANSACTIONID))
                .andExpect(status().isNotFound())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(404);
    }

    @Test
    void calculateRewardsForNewClientTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(REWARDS_API + "/" + NON_EXISTING_CLIENTID))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        RewardRS rewardRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), RewardRS.class);
        assertThat(rewardRS.getTotalRewardAmount()).isZero();
        assertThat(rewardRS.getMonthlyRewardAmount()).isEmpty();
    }

    @Test
    void addTransactionMissingAmountTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setAmount(null);
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo("Transaction amount is missing\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    void addTransactionWithoutBodyTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
    }
}
