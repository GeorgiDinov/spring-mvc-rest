package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.CustomerMapper;
import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.domain.Customer;
import com.georgidinov.springmvcrest.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(customerMapper, customerRepository);
    }

    @Test
    void getAllCustomers() {
        //given
        List<Customer> mockCustomers = new ArrayList<>();
        mockCustomers.add(Customer.builder().id(1L).firstName("John").lastName("Doe").build());
        mockCustomers.add(Customer.builder().id(2L).firstName("Jane").lastName("Smith").build());
        when(customerRepository.findAll()).thenReturn(mockCustomers);

        //when
        List<CustomerDTO> customerDTOS = this.customerService.getAllCustomers();

        //then
        assertEquals(customerDTOS.size(), 2);
    }

    @Test
    void findCustomerById() {
        //given
        Customer customer = Customer.builder().id(1L).firstName("John").lastName("Doe").build();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));

        //when
        CustomerDTO customerDTO = this.customerService.findCustomerById(1L);

        //then
        assertEquals("John", customerDTO.getFirstName());
    }

    @Test
    void createNewCustomer() {
        //given
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("John").lastName("Doe").build();
        Customer customer = Customer.builder().id(1L)
                .firstName(customerDTO.getFirstName()).lastName(customerDTO.getLastName()).build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        //when
        CustomerDTO savedDTO = this.customerService.createNewCustomer(customerDTO);

        //then
        assertEquals(customerDTO.getFirstName(), savedDTO.getFirstName());
        assertEquals("/api/v1/customers/1", savedDTO.getCustomerUrl());
    }
}