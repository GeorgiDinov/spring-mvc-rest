package com.georgidinov.springmvcrest.bootstrap;

import com.georgidinov.springmvcrest.domain.Category;
import com.georgidinov.springmvcrest.domain.Customer;
import com.georgidinov.springmvcrest.repository.CategoryRepository;
import com.georgidinov.springmvcrest.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        this.categoryRepository.save(Category.builder().name("Fruits").build());
        this.categoryRepository.save(Category.builder().name("Dried").build());
        this.categoryRepository.save(Category.builder().name("Fresh").build());
        this.categoryRepository.save(Category.builder().name("Exotic").build());
        this.categoryRepository.save(Category.builder().name("Nuts").build());
        log.info("Bootstrap Data Loaded Categories... we have {} records", this.categoryRepository.count());

        this.customerRepository.save(Customer.builder().firstName("John").lastName("Doe").build());
        this.customerRepository.save(Customer.builder().firstName("Jane").lastName("Smith").build());
        this.customerRepository.save(Customer.builder().firstName("Bob").lastName("Dilan").build());
        this.customerRepository.save(Customer.builder().firstName("Melinda").lastName("Harris").build());
        log.info("Bootstrap Data Loaded Customers... we have {} records", this.customerRepository.count());
    }

}