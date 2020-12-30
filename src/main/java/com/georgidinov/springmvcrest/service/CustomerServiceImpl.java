package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.CustomerMapper;
import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.api.v1.model.CustomerListDTO;
import com.georgidinov.springmvcrest.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerListDTO getAllCustomers() {
        return CustomerListDTO.builder().customerDTOS(
                this.customerRepository.findAll()
                        .stream()
                        .map(customerMapper::customerToCustomerDTO)
                        .collect(Collectors.toList())
        ).build();
    }

    @Override
    public CustomerDTO findCustomerById(Long id) {
        return this.customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO)
                .orElseThrow();
    }

}