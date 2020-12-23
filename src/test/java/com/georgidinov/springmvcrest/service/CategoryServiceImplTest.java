package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.CategoryMapper;
import com.georgidinov.springmvcrest.api.v1.model.CategoryDTO;
import com.georgidinov.springmvcrest.domain.Category;
import com.georgidinov.springmvcrest.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = "Mock_Name";


    @Mock
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository, CategoryMapper.INSTANCE);
    }

    @Test
    void getAllCategories() {
        //given
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder().name("Fruits").build());
        categories.add(Category.builder().name("Dried").build());
        categories.add(Category.builder().name("Fresh").build());
        categories.add(Category.builder().name("Exotic").build());
        categories.add(Category.builder().name("Nuts").build());

        when(this.categoryRepository.findAll()).thenReturn(categories);

        //when
        List<CategoryDTO> categoryDTOS = this.categoryService.getAllCategories();

        //then
        assertNotNull(categoryDTOS);
        assertEquals(5, categoryDTOS.size());
    }

    @Test
    void getCategoryByName() {
        //given
        Category category = Category.builder().id(ID).name(NAME).build();
        when(this.categoryRepository.findByName(anyString())).thenReturn(category);

        //when
        CategoryDTO categoryDTO = this.categoryService.getCategoryByName(NAME);

        //then
        assertEquals(ID, categoryDTO.getId());
        assertEquals(NAME, categoryDTO.getName());
    }
}