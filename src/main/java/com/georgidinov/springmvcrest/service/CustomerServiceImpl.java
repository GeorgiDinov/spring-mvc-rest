package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.CustomerMapper;
import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.domain.Customer;
import com.georgidinov.springmvcrest.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<CustomerDTO> getAllCustomers() {
        return this.customerRepository.findAll()
                .stream()
                .map(customer -> CustomerDTO.builder()
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .customerUrl("/api/v1/customers/" + customer.getId())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO findCustomerById(Long id) {
        return this.customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO)
                .orElseThrow();
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        return this.saveCustomer(this.customerMapper.customerDTOToCustomer(customerDTO));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = this.customerMapper.customerDTOToCustomer(customerDTO);
        customer.setId(id);
        return this.saveCustomer(customer);
    }


    private CustomerDTO saveCustomer(Customer customer) {
        Customer savedCustomer = this.customerRepository.save(customer);
        CustomerDTO customerDTO = this.customerMapper.customerToCustomerDTO(savedCustomer);
        customerDTO.setCustomerUrl("/api/v1/customers/" + savedCustomer.getId());
        return customerDTO;
    }
}