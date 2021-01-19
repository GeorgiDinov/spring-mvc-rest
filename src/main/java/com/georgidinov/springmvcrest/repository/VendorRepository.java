package com.georgidinov.springmvcrest.repository;

import com.georgidinov.springmvcrest.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

}