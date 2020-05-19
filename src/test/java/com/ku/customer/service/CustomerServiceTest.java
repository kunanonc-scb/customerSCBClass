package com.ku.customer.service;

import com.ku.customer.model.Customer;
import com.ku.customer.repositories.CustomerRepository;
import com.ku.customer.support.CustomerSupportTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerService(customerRepository);
    }

    @DisplayName("Test get all customer service")
    @Test
    void testGetAllCustomer() {
        when(customerRepository.findAll()).thenReturn(CustomerSupportTest.getCustomerList());
        List<Customer> resp = customerService.getCustomerList();
        assertEquals(1, resp.get(0).getId().intValue());
        assertEquals("Ryan", resp.get(0).getFirstName());
        assertEquals("Gosling", resp.get(0).getLastName());
        assertEquals("gosling@gmail.com", resp.get(0).getEmail());
        assertEquals("0912345678", resp.get(0).getPhoneNo());
        assertEquals(33, resp.get(0).getAge().intValue());

        assertEquals(2, resp.get(1).getId().intValue());
        assertEquals("David", resp.get(1).getFirstName());
        assertEquals("Beckham", resp.get(1).getLastName());
        assertEquals("beckham@gmail.com", resp.get(1).getEmail());
        assertEquals("0987654321", resp.get(1).getPhoneNo());
        assertEquals(23, resp.get(1).getAge().intValue());
    }

    @Test
    void testGetAllCustomerById() {
        Long reqParam = 1L;
        when(customerRepository.findAllById(reqParam)).thenReturn(CustomerSupportTest.getCustomerList().get(0));
        Customer resp = customerService.getCustomer(reqParam);

        assertEquals(1, resp.getId().intValue());
        assertEquals("Ryan", resp.getFirstName());
        assertEquals("Gosling", resp.getLastName());
        assertEquals("gosling@gmail.com", resp.getEmail());
        assertEquals("0912345678", resp.getPhoneNo());
        assertEquals(33, resp.getAge().intValue());
    }

    @Test
    void testGetCustomerByName() {
        String name = "Ryan";
        when(customerRepository.findByFirstName(name)).thenReturn(CustomerSupportTest.getCustomerByNameRyanList());
        List<Customer> resp = customerService.getCustomerByName(name);

        assertEquals(1, resp.get(0).getId().intValue());
        assertEquals("Ryan", resp.get(0).getFirstName());
        assertEquals("Gosling", resp.get(0).getLastName());
        assertEquals("gosling@gmail.com", resp.get(0).getEmail());
        assertEquals("0912345678", resp.get(0).getPhoneNo());
        assertEquals(33, resp.get(0).getAge().intValue());

        assertEquals(2, resp.get(1).getId().intValue());
        assertEquals("Ryan", resp.get(1).getFirstName());
        assertEquals("Reynold", resp.get(1).getLastName());
        assertEquals("reynold@gmail.com", resp.get(1).getEmail());
        assertEquals("0923456789", resp.get(1).getPhoneNo());
        assertEquals(27, resp.get(1).getAge().intValue());
    }

    @Test
    void testCreateCustomer() {
        Customer reqCustomer = new Customer();
        reqCustomer.setFirstName("Ryan");
        reqCustomer.setLastName("Gosling");
        reqCustomer.setPhoneNo("0912345678");
        reqCustomer.setEmail("gosling@gmail.com");
        reqCustomer.setAge(33);

        when(customerRepository.save(reqCustomer)).thenReturn(CustomerSupportTest.getNewCustomer());
        Customer resp = customerService.createCustomer(reqCustomer);

        assertEquals(1, resp.getId().intValue());
        assertEquals("Ryan", resp.getFirstName());
        assertEquals("Gosling", resp.getLastName());
        assertEquals("gosling@gmail.com", resp.getEmail());
        assertEquals("0912345678", resp.getPhoneNo());
        assertEquals(33, resp.getAge().intValue());
    }

    @Test
    void testUpdateCustomer() {
        Long reqId = 1L;
        Customer reqCustomer = new Customer();
        reqCustomer.setId(1L);
        reqCustomer.setFirstName("Ryan");
        reqCustomer.setLastName("Gosling");
        reqCustomer.setPhoneNo("0911111111");
        reqCustomer.setEmail("gosling@gmail.com");
        reqCustomer.setAge(33);

        when(customerRepository.findAllById(reqId)).thenReturn(CustomerSupportTest.getNewCustomer());
        when(customerRepository.save(reqCustomer)).thenReturn(CustomerSupportTest.getNewCustomer());
        Customer resp = customerService.updateCustomer(reqId, reqCustomer);

        assertEquals(1, resp.getId().intValue());
        assertEquals("Ryan", resp.getFirstName());
        assertEquals("Gosling", resp.getLastName());
        assertEquals("gosling@gmail.com", resp.getEmail());
        assertEquals("0912345678", resp.getPhoneNo());
        assertEquals(33, resp.getAge().intValue());
    }

    @Test
    void testUpdateCustomerFail() {
        Long reqId = 4L;
        Customer reqCustomer = new Customer();
        reqCustomer.setId(1L);
        reqCustomer.setFirstName("Ryan");
        reqCustomer.setLastName("Gosling");
        reqCustomer.setPhoneNo("0912345678");
        reqCustomer.setEmail("gosling@gmail.com");
        reqCustomer.setAge(33);

        when(customerRepository.findAllById(reqId)).thenReturn(null);
        Customer resp = customerService.updateCustomer(reqId, reqCustomer);
        assertEquals(null, resp);
    }

    @Test
    void testDeleteCustomer() {
        Long reqId = 1L;
        doNothing().when(customerRepository).deleteById(reqId);
        boolean resp = customerService.deleteById(reqId);
        assertTrue(resp);
    }

    @Test
    void testDeleteCustomerFail() {
        Long reqId = 1L;
        doThrow(EmptyResultDataAccessException.class).when(customerRepository).deleteById(reqId);
        boolean resp = customerService.deleteById(reqId);
        assertFalse(resp);
    }



}
