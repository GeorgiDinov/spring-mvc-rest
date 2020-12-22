package com.georgidinov.springmvcrest.bootstrap;

import com.georgidinov.springmvcrest.domain.Category;
import com.georgidinov.springmvcrest.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Autowired
    public Bootstrap(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        this.categoryRepository.save(Category.builder().name("Fruits").build());
        this.categoryRepository.save(Category.builder().name("Dried").build());
        this.categoryRepository.save(Category.builder().name("Fresh").build());
        this.categoryRepository.save(Category.builder().name("Exotic").build());
        this.categoryRepository.save(Category.builder().name("Nuts").build());
        log.info("Bootstrap Data Loaded... we have {} records", this.categoryRepository.count());
    }

}