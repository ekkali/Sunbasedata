package in.sunbasedata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.sunbasedata.data.ApiResponse;
import in.sunbasedata.model.Customer;
import in.sunbasedata.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Method to fetch paginated list of customers.
    // Returns a response with paginated customer data.
    @GetMapping("/getCustomers")
    public ResponseEntity<ApiResponse> getCustomers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size) {
        PageRequest pageable = PageRequest.of(page, size);

        try {
            Page<Customer> customers = customerService.getCustomersPageable(pageable);
            return new ResponseEntity<>(new ApiResponse(customers, true, "Fetched Successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, "Failed to fetch customers"), HttpStatus.OK);
        }
    }

    // Method to retrieve a customer by ID.
    @GetMapping("/customerById/{customerId}")
    public ResponseEntity<ApiResponse> getCustomerById(@PathVariable String customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer != null) {
            return new ResponseEntity<>(new ApiResponse(customer, true, "Fetched Successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, "Customer not found"), HttpStatus.NOT_FOUND);
        }
    }

    // Method to add a new customer.
    @PostMapping("/addCustomer")
    public ResponseEntity<ApiResponse> createCustomer(@RequestBody Customer customer) {
        customer.setId(UUID.randomUUID().toString());
        customerService.saveCustomer(customer);
        return new ResponseEntity<>(new ApiResponse(true, "Customer created successfully"), HttpStatus.CREATED);
    }

    // Method to update an existing customer.
    @PostMapping("/updateCustomer/{customerId}")
    public ResponseEntity<ApiResponse> updateCustomer(@PathVariable String  customerId, @RequestBody Customer updatedCustomer) {
        customerService.updateCustomer(customerId, updatedCustomer);
        return new ResponseEntity<>(new ApiResponse(true, "Customer updated successfully"), HttpStatus.OK);
    }

    // Method to delete a customer by ID.
    @GetMapping("/deleteCustomer/{customerId}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(new ApiResponse(true, "Customer deleted successfully"), HttpStatus.OK);
    }

    // Method to search for customers based on criteria and value.
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchCustomers(@RequestParam String criteria, @RequestParam String value) {
        try {
            List<Customer> customers = customerService.searchCustomers(criteria, value);
            return new ResponseEntity<>(new ApiResponse(customers, true, "Fetched Successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, "Search Fail"), HttpStatus.OK);
        }
    }


    @GetMapping ("/sync")
    public ResponseEntity<?>  addSunbaseCustomer() throws JsonProcessingException {
        customerService.saveSunbaseCustomer();
        return new ResponseEntity<>(new ApiResponse( true, "Sync Successfully"), HttpStatus.OK);
    }

}
