package com.ekta.telecom.RewardsCalculator.controller;

import com.ekta.telecom.RewardsCalculator.exception.ApiErrorResponse;
import com.ekta.telecom.RewardsCalculator.exception.ErrorMessage;
import com.ekta.telecom.RewardsCalculator.model.RewardRS;
import com.ekta.telecom.RewardsCalculator.model.Transaction;
import com.ekta.telecom.RewardsCalculator.model.TransactionRQRS;
import com.ekta.telecom.RewardsCalculator.util.TestUtility;
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
public class ControllerTests {

    private static final String TRANSACTION_API = "/manage/transaction";
    private static final String REWARDS_API = "/manage/rewards";
    private static final String CLIENT_ID = "CLIENT1";
    private static final String NON_EXISTING_CLIENT_ID = "CLIENT2";
    private static final long NON_EXISTING_TRANSACTION_ID = 75;
    private final ObjectMapper OBJECT_MAPPER = JsonMapper.builder().addModule(new JavaTimeModule()).build();
    List<Transaction> multipleTransactions = new ArrayList<>();
    List<Transaction> singleTransaction = new ArrayList<>();
    @Autowired
    private MockMvc mocTransactionController;

    @BeforeEach
    void setUp() {
        multipleTransactions = TestUtility.getMultipleTransactions();
        singleTransaction = TestUtility.getSingleTransaction();
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
        assertThat(transactionRQRS.getTransactions().get(0).getClientId()).isEqualTo(CLIENT_ID);
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
        assertThat(transactionRQRS.getTransactions().get(0).getClientId()).isEqualTo(multipleTransactions.get(0).getClientId());
        assertThat(transactionRQRS.getTransactions().get(0).getDate()).isEqualTo(multipleTransactions.get(0).getDate());
        assertThat(transactionRQRS.getTransactions().get(0).getCurrency()).isEqualTo(multipleTransactions.get(0).getCurrency());
        assertThat(transactionRQRS.getTransactions().get(0).getAmount()).isEqualByComparingTo(multipleTransactions.get(0).getAmount());

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
        assertThat(transactionRQRS.getTransactions().get(0).getClientId()).isEqualTo(CLIENT_ID);
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
        assertThat(transactionRQRS.getTransactions().get(0).getClientId()).isEqualTo(singleTransaction.get(0).getClientId());
        assertThat(transactionRQRS.getTransactions().get(0).getDate()).isEqualTo(singleTransaction.get(0).getDate());
        assertThat(transactionRQRS.getTransactions().get(0).getCurrency()).isEqualTo(singleTransaction.get(0).getCurrency());
        assertThat(transactionRQRS.getTransactions().get(0).getAmount()).isEqualByComparingTo(singleTransaction.get(0).getAmount());
    }

    @Test
    @Order(5)
    void calculateRewardsWithDefaultNoOfMonthsTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(REWARDS_API + "/" + CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        RewardRS rewardRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), RewardRS.class);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(rewardRS.getTotalRewardPoints()).isEqualTo(498);
        assertThat(rewardRS.getMonthlyRewardPoints()).hasSize(2);
        assertThat(rewardRS.getMonthlyRewardPoints())
                .containsEntry("June", 174L)
                .containsEntry("May", 324L);
    }

    @Test
    @Order(6)
    void calculateRewardsTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(REWARDS_API + "/" + CLIENT_ID)
                        .param("noOfMonths", "1"))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        RewardRS rewardRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), RewardRS.class);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(rewardRS.getTotalRewardPoints()).isEqualTo(174);
        assertThat(rewardRS.getMonthlyRewardPoints()).hasSize(1);
        assertThat(rewardRS.getMonthlyRewardPoints()).containsEntry("June", 174L);
    }

    @Test
    @Order(7)
    void calculateRewardsWithNegativeNoOfMonthsTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(REWARDS_API + "/" + CLIENT_ID)
                        .param("noOfMonths", "-1"))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        RewardRS rewardRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), RewardRS.class);
        assertThat(rewardRS.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(rewardRS.getTotalRewardPoints()).isZero();
        assertThat(rewardRS.getMonthlyRewardPoints()).isEmpty();
    }

    @Test
    void calculateRewardsForNewClientTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(REWARDS_API + "/" + NON_EXISTING_CLIENT_ID))
                .andExpect(status().isOk())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        RewardRS rewardRS = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), RewardRS.class);
        assertThat(rewardRS.getTotalRewardPoints()).isZero();
        assertThat(rewardRS.getMonthlyRewardPoints()).isEmpty();
    }

    @Test
    void calculateRewardsWithoutClientIdTest() throws Throwable {
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .get(REWARDS_API))
                .andExpect(status().isNotFound())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        assertThat(mcvResult.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    void modifyNonExistingTransaction() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setId(NON_EXISTING_TRANSACTION_ID);
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
                        .get(TRANSACTION_API + "/" + NON_EXISTING_TRANSACTION_ID))
                .andExpect(status().isNotFound())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.TRANSACTION_NOT_FOUND.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(404);
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
    void addTransactionMissingCurrencyTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setCurrency(null);
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo("Transaction currency is missing\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    void addTransactionInvalidCurrencyTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setCurrency("invalidCurrency");
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo("Invalid transaction currency\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    void addTransactionWithoutDateTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(singleTransaction);
        request.getTransactions().get(0).setDate(null);
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo("Transaction date is missing\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(400);
    }

    @Test
    void addTransactionWithEmptyTransactionTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(new ArrayList<>());
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo("Transactions cannot be null\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
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

    @Test
    void addTransactionsWithNegativeAmountTest() throws Throwable {
        TransactionRQRS request = new TransactionRQRS();
        request.setTransactions(TestUtility.getNegativeAmountTransaction());
        MvcResult mcvResult = mocTransactionController.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        mcvResult.getResponse().setCharacterEncoding("utf-8");
        ApiErrorResponse response = OBJECT_MAPPER.readValue(mcvResult.getResponse().getContentAsString(), ApiErrorResponse.class);
        assertThat(response.getErrorMessage()).isEqualTo("Invalid transaction amount\r\n" + ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorMessage());
        assertThat(response.getApiErrorMessage()).isEqualTo(ErrorMessage.CREATE_TRANSACTION_VALIDATION_ERROR.getErrorCode());
        assertThat(response.getHttpStatusCode()).isEqualTo(400);
    }
}
