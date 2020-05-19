package com.ku.customer.api;

import com.ku.customer.model.GetLoanInfoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoanApiTest {
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    LoanApi loanApi;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loanApi = new LoanApi(restTemplate);
    }

    public static ResponseEntity<String> prepareResponseSuccess() {
        return ResponseEntity.ok("{\n" +
                "    \"status\": {\n" +
                "        \"code\": \"0\",\n" +
                "        \"message\": \"success\"\n" +
                "    },\n" +
                "    \"data\": {\n" +
                "        \"id\": 1,\n" +
                "        \"status\": \"OK\",\n" +
                "        \"account_payable\": \"102-222-2200\",\n" +
                "        \"account_receivable\": \"102-333-2020\",\n" +
                "        \"principal_amount\": 3400000.0\n" +
                "    }\n" +
                "}");
    }

    public static ResponseEntity<String> prepareLOAN4002() {
        return ResponseEntity.ok("{\n" +
                "    \"status\": {\n" +
                "        \"code\": \"LOAN4002\",\n" +
                "        \"message\": \"Loan information not found\"\n" +
                "    },\n" +
                "    \"data\": \"GET_LOAN_INFO_NOT_FOUND\"\n" +
                "}");
    }

    public static ResponseEntity<String> prepareLOAN4001() {
        return ResponseEntity.ok("{\n" +
                "    \"status\": {\n" +
                "        \"code\": \"LOAN4001\",\n" +
                "        \"message\": \"Cannot get loan information\"\n" +
                "    },\n" +
                "    \"data\": \"Cannot get loan information\"\n" +
                "}");
    }

    @Test
    void testGetLoanInfo() throws Exception {
        Long reqId = 1L;
        when(restTemplate.exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()))
                .thenReturn(this.prepareResponseSuccess());

        GetLoanInfoResponse loanInfo = loanApi.getLoanInfo(reqId);

        assertEquals("1", loanInfo.getId().toString());
        assertEquals("102-222-2200", loanInfo.getAccountPayable());
        assertEquals("102-333-2020", loanInfo.getAccountReceivable());
        assertEquals("OK", loanInfo.getStatus());
        assertEquals(3400000, loanInfo.getPrincipalAmount());

        verify(restTemplate, times(1))
                .exchange(ArgumentMatchers.<RequestEntity<String>>any(),ArgumentMatchers.<Class<String>>any());
    }

    @Test
    void testGetLoanInfoLOAN4002() throws Exception {
        Long reqId = 2L;

        when(restTemplate.exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(this.prepareLOAN4002());

        GetLoanInfoResponse loanInfo = loanApi.getLoanInfo(reqId);

        assertEquals(null, loanInfo.getId());
        assertEquals(null, loanInfo.getAccountPayable());
        assertEquals(null, loanInfo.getAccountReceivable());
        assertEquals(null, loanInfo.getStatus());
        assertEquals(0, loanInfo.getPrincipalAmount());
    }

    @Test
    void testGetLoanInfoLOAN4001() throws Exception {
        Long reqId = 3L;

        when(restTemplate.exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenReturn(this.prepareLOAN4002());

        GetLoanInfoResponse loanInfo = loanApi.getLoanInfo(reqId);

        assertEquals(null, loanInfo.getId());
        assertEquals(null, loanInfo.getAccountPayable());
        assertEquals(null, loanInfo.getAccountReceivable());
        assertEquals(null, loanInfo.getStatus());
        assertEquals(0, loanInfo.getPrincipalAmount());
    }

    @Test
    void testGetLoanInfoClientException() {
        Long reqId = 3L;

        when(restTemplate.exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenThrow(HttpClientErrorException.class);

        Exception thrown = assertThrows(Exception.class,
                () -> loanApi.getLoanInfo(reqId),
                "Expected getLoanInfo(reqId) to throw, but it didn't");

        assertEquals("httpClientErrorException", thrown.getMessage());
        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        );
    }

    @Test
    void testGetLoanInfoServerException() {
        Long reqId = 3L;

        when(restTemplate.exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenThrow(HttpServerErrorException.class);

        Exception thrown = assertThrows(Exception.class,
                () -> loanApi.getLoanInfo(reqId),
                "Expected getLoanInfo(reqId) to throw, but it didn't");

        assertEquals("httpServerErrorException", thrown.getMessage());
        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        );
    }

    @Test
    void testGetLoanInfoCatchAllException() {
        Long reqId = 3L;

        when(restTemplate.exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        )).thenThrow(Exception.class);

        Exception thrown = assertThrows(Exception.class,
                () -> loanApi.getLoanInfo(reqId),
                "Expected getLoanInfo(reqId) to throw, but it didn't");

        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.<RequestEntity<String>>any(),
                ArgumentMatchers.<Class<String>>any()
        );
    }

}
