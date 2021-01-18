package com.georgidinov.springmvcrest.controller.v1;

import com.georgidinov.springmvcrest.api.v1.model.CustomerDTO;
import com.georgidinov.springmvcrest.controller.RestResponseEntityExceptionHandler;
import com.georgidinov.springmvcrest.exception.ResourceNotFoundException;
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

import static com.georgidinov.springmvcrest.controller.v1.AbstractRestControllerTest.asJsonString;
import static com.georgidinov.springmvcrest.util.ApplicationConstants.CUSTOMERS_BASE_URL;
import static com.georgidinov.springmvcrest.util.ApplicationConstants.SEPARATOR;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    void getAllCustomers() throws Exception {
        List<CustomerDTO> customerDTOS = new ArrayList<>();

        customerDTOS.add(CustomerDTO.builder()
                .firstName("Michale").lastName("Weston")
                .customerUrl(CUSTOMERS_BASE_URL + SEPARATOR + 1).build());
        customerDTOS.add(CustomerDTO.builder()
                .firstName("Sam").lastName("Axe")
                .customerUrl(CUSTOMERS_BASE_URL + SEPARATOR + 2).build());

        when(customerService.getAllCustomers()).thenReturn(customerDTOS);

        mockMvc.perform(get(CUSTOMERS_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerDTOS", hasSize(2)));
    }

    @Test
    void getCustomerById() throws Exception {

        CustomerDTO customer = CustomerDTO.builder()
                .firstName("Michale")
                .lastName("Weston")
                .customerUrl(CUSTOMERS_BASE_URL + SEPARATOR + 1)
                .build();

        when(customerService.findCustomerById(anyLong())).thenReturn(customer);

        mockMvc.perform(get(CUSTOMERS_BASE_URL + SEPARATOR + 1)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Michale")));
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDTO sentCustomerDTO = CustomerDTO.builder()
                .firstName("Michale")
                .lastName("Weston")
                .build();

        CustomerDTO returnedCustomerDTO = CustomerDTO.builder()
                .firstName(sentCustomerDTO.getFirstName())
                .lastName(sentCustomerDTO.getLastName())
                .customerUrl(CUSTOMERS_BASE_URL + SEPARATOR + 1)
                .build();

        when(customerService.updateCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnedCustomerDTO);

        mockMvc.perform(put(CUSTOMERS_BASE_URL + SEPARATOR + 1).contentType(MediaType.APPLICATION_JSON).content(asJsonString(sentCustomerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Michale")))
                .andExpect(jsonPath("$.lastName", equalTo("Weston")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMERS_BASE_URL + SEPARATOR + 1)));
    }

    @Test
    void patchCustomer() throws Exception {
        //given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("Fred");

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customer.getFirstName());
        returnDTO.setLastName("Flintstone");
        returnDTO.setCustomerUrl(CUSTOMERS_BASE_URL + SEPARATOR + 1);

        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(CUSTOMERS_BASE_URL + SEPARATOR + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Fred")))
                .andExpect(jsonPath("$.lastName", equalTo("Flintstone")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMERS_BASE_URL + SEPARATOR + 1)));
    }

    @Test
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete(CUSTOMERS_BASE_URL + SEPARATOR + 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomerById(anyLong());
    }

    @Test
    public void testNotFoundException() throws Exception {

        when(customerService.findCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CUSTOMERS_BASE_URL + "/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}