package com.georgidinov.springmvcrest.controller.v1;

import com.georgidinov.springmvcrest.api.v1.model.CategoryDTO;
import com.georgidinov.springmvcrest.api.v1.model.CategoryListDTO;
import com.georgidinov.springmvcrest.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.georgidinov.springmvcrest.util.ApplicationConstants.CATEGORIES_BASE_URL;

@Controller
@RequestMapping(CATEGORIES_BASE_URL)
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<CategoryListDTO> getAllCategories() {
        return new ResponseEntity<>
                (
                        new CategoryListDTO(categoryService.getAllCategories()),
                        HttpStatus.OK
                );
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable String name){
        return new ResponseEntity<>(categoryService.getCategoryByName(name), HttpStatus.OK);
    }
}
