package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.api.v1.model.CustomerListDTO;

public interface CustomerService {

    CustomerListDTO getAllCustomers();

    CustomerDTO findCustomerById(Long id);

}