package com.ku.customer.controller;

import com.ku.customer.api.LoanApi;
import com.ku.customer.support.LoanSupportTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LoanControllerTest {

    @Mock
    LoanApi loanApi;

    @InjectMocks
    LoanController loanController;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loanController = new LoanController(loanApi);
        mvc = MockMvcBuilders.standaloneSetup(loanController)
                .build();
    }

    @Test
    void testGetLoanById() throws Exception {
        Long reqId = 1L;
        when(loanApi.getLoanInfo(reqId))
                .thenReturn(LoanSupportTest.getNewLoanInfo());

        MvcResult mvcResult = mvc.perform(get( "/loan/" + reqId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("1", jsonObject.get("id").toString());
        assertEquals("102-222-2200", jsonObject.get("account_payable"));
        assertEquals("102-333-2200", jsonObject.get("account_receivable"));
        assertEquals("OK", jsonObject.get("status"));
        assertEquals(3400000.0, jsonObject.get("principal_amount"));
    }
}
