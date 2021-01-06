package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.CustomerMapper;
import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.domain.Customer;
import com.georgidinov.springmvcrest.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    //== fields ==
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    //== constructors ==
    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }


    //== public methods ==
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
                .orElseThrow();// todo custom exception
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

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {

            if (customerDTO.getFirstName() != null) {
                customer.setFirstName(customerDTO.getFirstName());
            }

            if (customerDTO.getLastName() != null) {
                customer.setLastName(customerDTO.getLastName());
            }

            CustomerDTO customerDTO1 = customerMapper.customerToCustomerDTO(customerRepository.save(customer));
            customerDTO1.setCustomerUrl("/api/v1/customers/" + id);
            return customerDTO1;
        }).orElseThrow();// todo custom exception
    }

    @Override
    public void deleteCustomerById(Long id) {
        this.customerRepository.deleteById(id);
    }

    //== private methods ==
    private CustomerDTO saveCustomer(Customer customer) {
        Customer savedCustomer = this.customerRepository.save(customer);
        CustomerDTO customerDTO = this.customerMapper.customerToCustomerDTO(savedCustomer);
        customerDTO.setCustomerUrl("/api/v1/customers/" + savedCustomer.getId());
        return customerDTO;
    }
}