package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.CustomerMapper;
import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.bootstrap.Bootstrap;
import com.georgidinov.springmvcrest.domain.Customer;
import com.georgidinov.springmvcrest.repository.CategoryRepository;
import com.georgidinov.springmvcrest.repository.CustomerRepository;
import com.georgidinov.springmvcrest.repository.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CustomerServiceImplIT {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    VendorRepository vendorRepository;

    CustomerService customerService;

    @BeforeEach
    public void setUp() throws Exception {
        Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository);
        bootstrap.run();
        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository);
    }


    @Test
    public void patchCustomerUpdateFirstName() throws Exception {
        String updatedName = "UpdatedName";
        long id = getCustomerIdValue();

        Customer originalCustomer = customerRepository.getOne(id);
        assertNotNull(originalCustomer);
        //save original first name
        String originalFirstName = originalCustomer.getFirstName();
        String originalLastName = originalCustomer.getLastName();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(updatedName);

        customerService.patchCustomer(id, customerDTO);

        Customer updatedCustomer = customerRepository.findById(id).get();

        assertNotNull(updatedCustomer);
        assertEquals(updatedName, updatedCustomer.getFirstName());
        assertNotEquals(originalFirstName, updatedCustomer.getFirstName());
        assertEquals(originalLastName, updatedCustomer.getLastName());
    }

    @Test
    public void patchCustomerUpdateLastName() throws Exception {
        String updatedName = "UpdatedName";
        long id = getCustomerIdValue();

        Customer originalCustomer = customerRepository.getOne(id);
        assertNotNull(originalCustomer);

        //save original first/last name
        String originalFirstName = originalCustomer.getFirstName();
        String originalLastName = originalCustomer.getLastName();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName(updatedName);

        customerService.patchCustomer(id, customerDTO);

        Customer updatedCustomer = customerRepository.findById(id).get();

        assertAll(
                "Multiple Tests",
                () -> assertNotNull(updatedCustomer),
                () -> assertEquals(updatedName, updatedCustomer.getLastName()),
                () -> assertEquals(originalFirstName, updatedCustomer.getFirstName()),
                () -> assertNotEquals(originalLastName, updatedCustomer.getLastName())
        );
    }

    private Long getCustomerIdValue() {
        List<Customer> customers = customerRepository.findAll();
        log.info("Customers Found: {}", customers.size());

        //return first id
        return customers.get(0).getId();
    }

}
