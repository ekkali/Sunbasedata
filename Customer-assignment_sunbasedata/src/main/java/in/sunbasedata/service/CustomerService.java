package in.sunbasedata.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.sunbasedata.model.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerService {

    // Get a paginated list of customers.
    Page<Customer> getCustomersPageable(Pageable pageable);

    // Get a customer by ID.
    Customer getCustomerById(String customerId);

    // Save a new customer.
    void saveCustomer(Customer customer);

    // Update an existing customer.
    void updateCustomer(String customerId, Customer customer);

    // Delete a customer by ID.
    void deleteCustomer(String customerId);

    // Search for customers based on criteria and value.
    List<Customer> searchCustomers(String criteria, String value);

    // Save sunbase customer
    void saveSunbaseCustomer() throws JsonProcessingException;

}

