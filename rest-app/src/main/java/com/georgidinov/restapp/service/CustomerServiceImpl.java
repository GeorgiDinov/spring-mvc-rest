package com.georgidinov.restapp.service;

import com.georgidinov.restapp.api.v1.mapper.CustomerMapper;
import com.georgidinov.restapp.api.v1.model.CustomerDTO;
import com.georgidinov.restapp.domain.Customer;
import com.georgidinov.restapp.exception.ResourceNotFoundException;
import com.georgidinov.restapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.georgidinov.restapp.util.ApplicationConstants.CUSTOMERS_BASE_URL;
import static com.georgidinov.restapp.util.ApplicationConstants.SEPARATOR;

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
                        .customerUrl(CUSTOMERS_BASE_URL + SEPARATOR + customer.getId())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO findCustomerById(Long id) {
        CustomerDTO customerDTO = this.customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO).orElseThrow(ResourceNotFoundException::new);
        customerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + SEPARATOR + id);
        return customerDTO;
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
            customerDTO1.setCustomerUrl(CUSTOMERS_BASE_URL + SEPARATOR + id);
            return customerDTO1;
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteCustomerById(Long id) {
        this.customerRepository.deleteById(id);
    }

    //== private methods ==
    private CustomerDTO saveCustomer(Customer customer) {
        Customer savedCustomer = this.customerRepository.save(customer);
        CustomerDTO customerDTO = this.customerMapper.customerToCustomerDTO(savedCustomer);
        customerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + SEPARATOR + savedCustomer.getId());
        return customerDTO;
    }
}