package com.ku.customer.support;

import com.ku.customer.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerSupportTest {
    public static List<Customer> getCustomerList() {
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Ryan");
        customer.setLastName("Gosling");
        customer.setEmail("gosling@gmail.com");
        customer.setPhoneNo("0912345678");
        customer.setAge(33);
        customers.add(customer);

        customer = new Customer();
        customer.setId(2L);
        customer.setFirstName("David");
        customer.setLastName("Beckham");
        customer.setEmail("beckham@gmail.com");
        customer.setPhoneNo("0987654321");
        customer.setAge(23);
        customers.add(customer);
        return customers;
    }

    public static List<Customer> getCustomerByNameRyanList() {
        List<Customer> customers = new ArrayList<>();
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Ryan");
        customer.setLastName("Gosling");
        customer.setEmail("gosling@gmail.com");
        customer.setPhoneNo("0912345678");
        customer.setAge(33);
        customers.add(customer);

        customer = new Customer();
        customer.setId(2L);
        customer.setFirstName("Ryan");
        customer.setLastName("Reynold");
        customer.setEmail("reynold@gmail.com");
        customer.setPhoneNo("0923456789");
        customer.setAge(27);
        customers.add(customer);
        return customers;
    }

    public static Customer getNewCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Ryan");
        customer.setLastName("Gosling");
        customer.setEmail("gosling@gmail.com");
        customer.setPhoneNo("0912345678");
        customer.setAge(33);
        return customer;
    }

    public static Customer createNewCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Emma");
        customer.setLastName("Stone");
        customer.setEmail("stone@gmail.com");
        customer.setPhoneNo("0922222222");
        customer.setAge(29);
        return customer;
    }

    public static Customer respCreateNewCustomer() {
        Customer customer = new Customer();
        customer.setId(9L);
        customer.setFirstName("Emma");
        customer.setLastName("Stone");
        customer.setEmail("stone@gmail.com");
        customer.setPhoneNo("0922222222");
        customer.setAge(29);
        return customer;
    }

    public static Customer getUpdateCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Emma");
        customer.setLastName("Watson");
        customer.setEmail("watson@gmail.com");
        customer.setPhoneNo("0922222222");
        customer.setAge(29);
        return customer;
    }
}
