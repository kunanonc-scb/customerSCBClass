package com.ku.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ku.customer.model.Customer;
import com.ku.customer.service.CustomerService;
import com.ku.customer.support.CustomerSupportTest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    private MockMvc mvc;
    public static final String urlCustomerList = "/customers/list/";
    public static final String urlCustomer = "/customers/";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerController = new CustomerController(customerService);
        mvc = MockMvcBuilders.standaloneSetup(customerController)
                .build();
    }

    @Test
    void testGetCustomerList() throws Exception {
        when(customerService.getCustomerList()).thenReturn(CustomerSupportTest.getCustomerList());

        MvcResult mvcResult = mvc.perform(get(urlCustomerList))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertEquals("1", jsonArray.getJSONObject(0).get("id").toString());
        assertEquals("Ryan", jsonArray.getJSONObject(0).get("firstName"));
        assertEquals("Gosling", jsonArray.getJSONObject(0).get("lastName"));
        assertEquals("gosling@gmail.com", jsonArray.getJSONObject(0).get("email"));
        assertEquals("0912345678", jsonArray.getJSONObject(0).get("phoneNo"));
        assertEquals(33, jsonArray.getJSONObject(0).get("age"));

        assertEquals("2", jsonArray.getJSONObject(1).get("id").toString());
        assertEquals("David", jsonArray.getJSONObject(1).get("firstName"));
        assertEquals("Beckham", jsonArray.getJSONObject(1).get("lastName"));
        assertEquals("beckham@gmail.com", jsonArray.getJSONObject(1).get("email"));
        assertEquals("0987654321", jsonArray.getJSONObject(1).get("phoneNo"));
        assertEquals(23, jsonArray.getJSONObject(1).get("age"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        Long reqId = 1L;
        when(customerService.getCustomer(reqId)).thenReturn(CustomerSupportTest.getNewCustomer());

        MvcResult mvcResult = mvc.perform(get(urlCustomer + "/" + reqId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("1", jsonObject.get("id").toString());
        assertEquals("Ryan", jsonObject.get("firstName"));
        assertEquals("Gosling", jsonObject.get("lastName"));
        assertEquals("gosling@gmail.com", jsonObject.get("email"));
        assertEquals("0912345678", jsonObject.get("phoneNo"));
        assertEquals(33, jsonObject.get("age"));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {
        Long reqId = 5L;
        MvcResult mvcResult = mvc.perform(get(urlCustomer + "/" + reqId))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testGetCustomerByNameList() throws Exception {
        String name = "Ryan";
        when(customerService.getCustomerByName(name)).
                thenReturn(CustomerSupportTest.getCustomerByNameRyanList());

        MvcResult mvcResult = mvc.perform(get(urlCustomer + "?name=" + name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        JSONArray jsonArray = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertEquals("1", jsonArray.getJSONObject(0).get("id").toString());
        assertEquals("Ryan", jsonArray.getJSONObject(0).get("firstName"));
        assertEquals("Gosling", jsonArray.getJSONObject(0).get("lastName"));
        assertEquals("gosling@gmail.com", jsonArray.getJSONObject(0).get("email"));
        assertEquals("0912345678", jsonArray.getJSONObject(0).get("phoneNo"));
        assertEquals(33, jsonArray.getJSONObject(0).get("age"));

        assertEquals("2", jsonArray.getJSONObject(1).get("id").toString());
        assertEquals("Ryan", jsonArray.getJSONObject(1).get("firstName"));
        assertEquals("Reynold", jsonArray.getJSONObject(1).get("lastName"));
        assertEquals("reynold@gmail.com", jsonArray.getJSONObject(1).get("email"));
        assertEquals("0923456789", jsonArray.getJSONObject(1).get("phoneNo"));
        assertEquals(27, jsonArray.getJSONObject(1).get("age"));
    }

    @Test
    void testGetCustomerByNameWithEmpty() throws Exception {
        String name = "Ryan";
        List<Customer> list = CustomerSupportTest.getCustomerByNameRyanList();
        list.clear();
        when(customerService.getCustomerByName(name)).
                thenReturn(list);

        MvcResult mvcResult = mvc.perform(get(urlCustomer + "?name=" + name))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testGetCustomerByNameWithNull() throws Exception {
        String name = "Ryan";
        when(customerService.getCustomerByName(name)).
                thenReturn(null);

        MvcResult mvcResult = mvc.perform(get(urlCustomer + "?name=" + name))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testGetCustomerByNameNotFound() throws Exception {
        String name = "NotRyan";
        MvcResult mvcResult = mvc.perform(get(urlCustomer + "?name=" + name))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testCreateCustomer() throws Exception {
        Customer reqCustomer = CustomerSupportTest.createNewCustomer();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(reqCustomer);

        when(customerService.createCustomer(reqCustomer)).thenReturn(
                CustomerSupportTest.respCreateNewCustomer());

        MvcResult mvcResult = mvc.perform(post(urlCustomer)
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated()).andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("9", jsonObject.get("id").toString());
        assertEquals("Emma", jsonObject.get("firstName"));
        assertEquals("Stone", jsonObject.get("lastName"));
        assertEquals("stone@gmail.com", jsonObject.get("email"));
        assertEquals("0922222222", jsonObject.get("phoneNo"));
        assertEquals(29, jsonObject.get("age"));
    }

    @Test
    void testCreateCustomerWithFirstNameEmpty() throws Exception {
        Customer reqCustomer = CustomerSupportTest.createNewCustomer();
        reqCustomer.setFirstName("");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(reqCustomer);

        when(customerService.createCustomer(reqCustomer)).thenReturn(
                CustomerSupportTest.respCreateNewCustomer());

        MvcResult mvcResult = mvc.perform(post(urlCustomer)
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals("Validation failed for argument [0] in public org.springframework.http.ResponseEntity<?> com.ku.customer.controller.CustomerController.createCustomer(com.ku.customer.model.Customer): [Field error in object 'customer' on field 'firstName': rejected value []; codes [Size.customer.firstName,Size.firstName,Size.java.lang.String,Size]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [customer.firstName,firstName]; arguments []; default message [firstName],100,1]; default message [Please type your first name size between 1 - 100]] ", mvcResult.getResolvedException().getMessage());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer reqCustomer = CustomerSupportTest.getUpdateCustomer();
        Long reqId = 1L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(reqCustomer);

        when(customerService.updateCustomer(reqId, reqCustomer))
                .thenReturn(CustomerSupportTest.getUpdateCustomer());

        MvcResult mvcResult = mvc.perform(put(urlCustomer + "/" + reqId)
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk()).andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertEquals("1", jsonObject.get("id").toString());
        assertEquals("Emma", jsonObject.get("firstName"));
        assertEquals("Watson", jsonObject.get("lastName"));
        assertEquals("watson@gmail.com", jsonObject.get("email"));
        assertEquals("0922222222", jsonObject.get("phoneNo"));
        assertEquals(29, jsonObject.get("age"));
    }

    @Test
    void testUpdateCustomerNotFound() throws Exception {
        Customer reqCustomer = CustomerSupportTest.getUpdateCustomer();
        Long reqId = 1L;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(reqCustomer);

        when(customerService.updateCustomer(reqId, reqCustomer))
                .thenReturn(null);

        MvcResult mvcResult = mvc.perform(put(urlCustomer + "/" + reqId)
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isNotFound()).andReturn();

        verify(customerService, times(1)).updateCustomer(reqId, reqCustomer);
    }

    @Test
    void testUpdateCustomerWithPathNotComplete() throws Exception {
        Customer reqCustomer = CustomerSupportTest.getUpdateCustomer();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(reqCustomer);


        MvcResult mvcResult = mvc.perform(put(urlCustomer + "/")
                .contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isMethodNotAllowed()).andReturn();

    }

    @Test
    void testDeleteCustomer() throws Exception {
        Long reqId = 4L;
        when(customerService.deleteById(reqId)).thenReturn(true);

        MvcResult mvcResult = mvc.perform(delete(urlCustomer + "/" + reqId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        verify(customerService, times(1)).deleteById(reqId);
    }

    @Test
    void testDeleteCustomerNotFound() throws Exception {
        Long reqId = 4L;
        when(customerService.deleteById(reqId)).thenReturn(false);

        MvcResult mvcResult = mvc.perform(delete(urlCustomer + "/" + reqId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();

        verify(customerService, times(1)).deleteById(reqId);
    }
}
