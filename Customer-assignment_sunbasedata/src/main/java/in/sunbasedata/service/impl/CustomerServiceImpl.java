package in.sunbasedata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.sunbasedata.model.Customer;
import in.sunbasedata.repository.CustomerRepository;
import in.sunbasedata.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${sunbase.user.name}")
    private String username;

    @Value("${sunbase.user.password}")
    private String password;

    @Value("${sunbase.api.token}")
    private String tokenApiUrl;

    @Value("${sunbase.api.customer}")
    private String customerApiUrl;

    @Override
    public Page<Customer> getCustomersPageable(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer getCustomerById(String customerId) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        return optionalCustomer.orElse(null);
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomer(String customerId, Customer updatedCustomer) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setFirstName(updatedCustomer.getFirstName());
            customer.setLastName(updatedCustomer.getLastName());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setPhone(updatedCustomer.getPhone());
            customer.setAddress(updatedCustomer.getAddress());
            customer.setStreet(updatedCustomer.getStreet());
            customer.setCity(updatedCustomer.getCity());
            customer.setState(updatedCustomer.getState());
            customerRepository.save(customer);
        }
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<Customer> searchCustomers(String criteria, String value) {
        switch (criteria) {
            case "firstName":
                return customerRepository.findByFirstNameContaining(value);
            case "email":
                return customerRepository.findByEmailContaining(value);
            case "phone":
                return customerRepository.findByPhoneContaining(value);
            case "city":
                return customerRepository.findByCityContaining(value);
            default:
                throw new IllegalArgumentException("Invalid search criteria");
        }
    }


    public String getSunbaseToken(String login_id, String password, String apiUrl) {
        try {
            String requestBody = String.format("{\"login_id\": \"%s\", \"password\": \"%s\"}", login_id, password);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
                return jsonNode.get("access_token").asText();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String synSunbaseCustomer() {
        try {
            String token= getSunbaseToken(username,password,tokenApiUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(customerApiUrl, HttpMethod.GET, entity, String.class);
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                String response = responseEntity.getBody();
                return response;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveSunbaseCustomer() throws JsonProcessingException {
        String response = synSunbaseCustomer();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        for (JsonNode customerNode : jsonNode) {
            String uuid = customerNode.get("uuid").asText();
            String firstName = customerNode.get("first_name").asText();
            String lastName = customerNode.get("last_name").asText();
            String street = customerNode.get("street").asText();
            String address = customerNode.get("address").asText();
            String city = customerNode.get("city").asText();
            String state = customerNode.get("state").asText();
            String email = customerNode.get("email").asText();
            String phone = customerNode.get("phone").asText();

//            Optional<Customer> existingCustomerOptional= customerRepository.findByIdAndEmail(uuid,email);
//            List<Customer> existingCustomerByEmail = customerRepository.findByEmail(email);
//            Optional<Customer> existingCustomerOptional = customerRepository.findById(uuid);
            Optional<Customer> existingCustomerOptional=customerRepository.findCustomerByEmail(email);

            if (existingCustomerOptional.isPresent()) {
                Customer existingCustomer = existingCustomerOptional.get();
                existingCustomer.setFirstName(firstName);
                existingCustomer.setLastName(lastName);
                existingCustomer.setStreet(street);
                existingCustomer.setAddress(address);
                existingCustomer.setCity(city);
                existingCustomer.setState(state);
                existingCustomer.setPhone(phone);
                updateCustomer(uuid, existingCustomer);
            } else {
                Customer newCustomer = new Customer();
                newCustomer.setId(uuid);
                newCustomer.setFirstName(firstName);
                newCustomer.setLastName(lastName);
                newCustomer.setStreet(street);
                newCustomer.setAddress(address);
                newCustomer.setCity(city);
                newCustomer.setState(state);
                newCustomer.setEmail(email);
                newCustomer.setPhone(phone);
                saveCustomer(newCustomer);
            }
        }

    }


}
