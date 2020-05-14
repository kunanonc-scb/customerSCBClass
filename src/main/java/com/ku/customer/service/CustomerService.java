package com.ku.customer.service;

import com.ku.customer.model.Customer;
import com.ku.customer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getCustomerList() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findAllById(id);
    }

    public List<Customer> getCustomerByName(String name) {
        return customerRepository.findByFirstName(name);
    }

    public Customer createCustomer(Customer body) {
        return customerRepository.save(body);
    }

    public Customer updateCustomer(Long id, Customer body) {
        Customer customer = customerRepository.findAllById(id);
        return customer != null ?
                customerRepository.save(body) :
                null;
    }

    public boolean deleteById(Long id) {
        try {
            customerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
