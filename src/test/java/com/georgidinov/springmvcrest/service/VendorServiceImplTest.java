package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.VendorMapper;
import com.georgidinov.springmvcrest.api.v1.model.VendorDTO;
import com.georgidinov.springmvcrest.domain.Vendor;
import com.georgidinov.springmvcrest.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.georgidinov.springmvcrest.util.ApplicationConstants.SEPARATOR;
import static com.georgidinov.springmvcrest.util.ApplicationConstants.VENDORS_BASE_URL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VendorServiceImplTest {

    @Mock
    VendorRepository vendorRepository;

    VendorService vendorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vendorService = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);
    }

    @Test
    void getAllVendors() {
        //given
        List<Vendor> mockVendors = new ArrayList<>();
        mockVendors.add(Vendor.builder().id(1L).name("Test Vendor One").build());
        mockVendors.add(Vendor.builder().id(2L).name("Test Vendor Two").build());

        when(this.vendorRepository.findAll()).thenReturn(mockVendors);

        //when
        List<VendorDTO> vendorDTOS = vendorService.getAllVendors();

        //then
        assertEquals(2L, vendorDTOS.size());
    }

    @Test
    void findVendorById() {
        //given
        Optional<Vendor> vendor = Optional.ofNullable(Vendor.builder().id(1L).name("Test Vendor Name").build());
        when(vendorRepository.findById(anyLong())).thenReturn(vendor);

        //when
        VendorDTO vendorDTO = vendorService.findVendorById(1L);

        //then
        assertAll(
                "findVendorById() Assertions",
                () -> assertEquals("Test Vendor Name", vendorDTO.getName()),
                () -> assertEquals(VENDORS_BASE_URL + SEPARATOR + 1L, vendorDTO.getVendorUrl())
        );
    }

    @Test
    void createNewVendor() {
        //given
        Vendor vendor = Vendor.builder().id(1L).name("Test Vendor Name").build();
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendor);

        //when
        VendorDTO vendorDTO = vendorService.createNewVendor(VendorDTO.builder().name(vendor.getName()).build());

        //then
        assertAll(
                "createNewVendor() Assertions: ",
                () -> assertNotNull(vendorDTO),
                () -> assertEquals(vendor.getName(), vendorDTO.getName()),
                () -> assertEquals(VENDORS_BASE_URL + SEPARATOR + 1L, vendorDTO.getVendorUrl())
        );
    }

    @Test
    void updateVendor() {
        //given
        Optional<Vendor> vendor = Optional.ofNullable(Vendor.builder().id(1L).name("Test Vendor Name").build());
        VendorDTO vendorDTO = VendorDTO.builder().name("Updated Test Vendor Name").build();
        when(this.vendorRepository.findById(anyLong())).thenReturn(vendor);
        when(this.vendorRepository.save(any(Vendor.class))).thenReturn(vendor.get());

        //when
        VendorDTO updatedVendorDTO = this.vendorService.updateVendor(1L, vendorDTO);

        //then
        assertAll(
                "updateVendor() Assertions",
                () -> assertNotNull(updatedVendorDTO),
                () -> assertEquals(updatedVendorDTO.getName(), vendorDTO.getName()),
                () -> assertEquals(updatedVendorDTO.getVendorUrl(), VENDORS_BASE_URL + SEPARATOR + 1L)
        );
    }

    @Test
    void patchVendor() {
        //given
        Optional<Vendor> vendor = Optional.ofNullable(Vendor.builder().id(1L).name("Test Vendor Name").build());
        VendorDTO vendorDTO = VendorDTO.builder().name(null).build();

        when(this.vendorRepository.findById(anyLong())).thenReturn(vendor);
        when(this.vendorRepository.save(any(Vendor.class))).thenReturn(vendor.get());

        //when
        VendorDTO updatedVendorDTO = this.vendorService.updateVendor(1L, vendorDTO);

        //then
        assertAll(
                "updateVendor() Assertions",
                () -> assertNotNull(updatedVendorDTO),
                () -> assertEquals(updatedVendorDTO.getName(), vendor.get().getName()),
                () -> assertEquals(updatedVendorDTO.getVendorUrl(), VENDORS_BASE_URL + SEPARATOR + 1L)
        );
    }

    @Test
    void deleteVendorById() {
        vendorService.deleteVendorById(1L);
        verify(vendorRepository).deleteById(anyLong());
    }
}