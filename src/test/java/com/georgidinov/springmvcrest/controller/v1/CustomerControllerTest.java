package com.georgidinov.springmvcrest.controller.v1;

import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void getAllCustomers() throws Exception {
        List<CustomerDTO> customerDTOS = new ArrayList<>();

        customerDTOS.add(CustomerDTO.builder()
                .firstName("Michale").lastName("Weston")
                .customerUrl("/api/v1/customers/1").build());
        customerDTOS.add(CustomerDTO.builder()
                .firstName("Sam").lastName("Axe")
                .customerUrl("/api/v1/customers/2").build());

        when(customerService.getAllCustomers()).thenReturn(customerDTOS);

        mockMvc.perform(get("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerDTOS", hasSize(2)));
    }

    @Test
    void getCustomerById() throws Exception {

        CustomerDTO customer = CustomerDTO.builder()
                .firstName("Michale")
                .lastName("Weston")
                .customerUrl("/api/v1/customers/1")
                .build();

        when(customerService.findCustomerById(anyLong())).thenReturn(customer);

        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Michale")));
    }
}