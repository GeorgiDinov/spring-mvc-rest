package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;

import java.util.List;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO findCustomerById(Long id);

}