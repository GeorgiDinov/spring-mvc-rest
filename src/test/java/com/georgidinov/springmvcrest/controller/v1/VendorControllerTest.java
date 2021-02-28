package com.georgidinov.springmvcrest.controller.v1;

import com.georgidinov.springmvcrest.api.v1.model.VendorDTO;
import com.georgidinov.springmvcrest.controller.RestResponseEntityExceptionHandler;
import com.georgidinov.springmvcrest.service.VendorService;
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

import static com.georgidinov.springmvcrest.util.ApplicationConstants.SEPARATOR;
import static com.georgidinov.springmvcrest.util.ApplicationConstants.VENDORS_BASE_URL;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VendorControllerTest extends AbstractRestControllerTest {

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    void getAllVendors() throws Exception {
        //given
        List<VendorDTO> vendorDTOS = new ArrayList<>();
        vendorDTOS.add(VendorDTO.builder()
                .name("Test Vendor DTO One")
                .vendorUrl(VENDORS_BASE_URL + SEPARATOR + 1)
                .build());
        vendorDTOS.add(VendorDTO.builder()
                .name("Test Vendor DTO Two")
                .vendorUrl(VENDORS_BASE_URL + SEPARATOR + 2)
                .build());

        when(vendorService.getAllVendors()).thenReturn(vendorDTOS);

        //when then
        mockMvc.perform(get(VENDORS_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorDTOS", hasSize(2)));

    }

    @Test
    void getVendorById() throws Exception {
        //given
        VendorDTO vendor = VendorDTO.builder()
                .name("Test Vendor DTO")
                .vendorUrl(VENDORS_BASE_URL + SEPARATOR + 1)
                .build();

        when(vendorService.findVendorById(anyLong())).thenReturn(vendor);

        mockMvc.perform(get(VENDORS_BASE_URL + SEPARATOR + 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendor.getName())));
    }

    @Test
    void createNewVendor() throws Exception {
        //given
        VendorDTO sentVendorDTO = VendorDTO.builder().name("Test Vendor DTO").build();
        VendorDTO returnedVendorDTO = VendorDTO.builder().name(sentVendorDTO.getName())
                .vendorUrl(VENDORS_BASE_URL + SEPARATOR + 1)
                .build();
        when(vendorService.createNewVendor(any(VendorDTO.class))).thenReturn(returnedVendorDTO);

        mockMvc.perform(post(VENDORS_BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sentVendorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(sentVendorDTO.getName())))
                .andExpect(jsonPath("$.vendorUrl", equalTo(returnedVendorDTO.getVendorUrl())));
    }

    @Test
    void updateVendor() throws Exception {
        //given
        VendorDTO sentVendorDTO = VendorDTO.builder().name("Test Updated Vendor DTO").build();
        VendorDTO returnedVendorDTO = VendorDTO.builder().name(sentVendorDTO.getName())
                .vendorUrl(VENDORS_BASE_URL + SEPARATOR + 1)
                .build();
        when(vendorService.updateVendor(anyLong(), any(VendorDTO.class))).thenReturn(returnedVendorDTO);

        mockMvc.perform(put(VENDORS_BASE_URL + SEPARATOR + 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sentVendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(sentVendorDTO.getName())))
                .andExpect(jsonPath("$.vendorUrl", equalTo(returnedVendorDTO.getVendorUrl())));
    }

    @Test
    void patchVendor() throws Exception {
        //given
        VendorDTO sentVendorDTO = VendorDTO.builder().name("Test Updated Vendor DTO").build();
        VendorDTO returnedVendorDTO = VendorDTO.builder().name(sentVendorDTO.getName())
                .vendorUrl(VENDORS_BASE_URL + SEPARATOR + 1)
                .build();
        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(returnedVendorDTO);

        mockMvc.perform(patch(VENDORS_BASE_URL + SEPARATOR + 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sentVendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(sentVendorDTO.getName())))
                .andExpect(jsonPath("$.vendorUrl", equalTo(returnedVendorDTO.getVendorUrl())));
    }

    @Test
    void deleteVendor() throws Exception {
        mockMvc.perform(delete(VENDORS_BASE_URL + SEPARATOR + 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(vendorService).deleteVendorById(anyLong());
    }
}