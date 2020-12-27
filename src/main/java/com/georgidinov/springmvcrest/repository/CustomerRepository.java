package com.georgidinov.springmvcrest.repository;

import com.georgidinov.springmvcrest.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}