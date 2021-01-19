package com.georgidinov.springmvcrest.service;

import com.georgidinov.springmvcrest.api.v1.mapper.VendorMapper;
import com.georgidinov.springmvcrest.api.v1.model.VendorDTO;
import com.georgidinov.springmvcrest.domain.Vendor;
import com.georgidinov.springmvcrest.exception.ResourceNotFoundException;
import com.georgidinov.springmvcrest.repository.VendorRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.georgidinov.springmvcrest.util.ApplicationConstants.SEPARATOR;
import static com.georgidinov.springmvcrest.util.ApplicationConstants.VENDORS_BASE_URL;

@Slf4j
@AllArgsConstructor
@Service
public class VendorServiceImpl implements VendorService {


    //== fields ==
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Override
    public List<VendorDTO> getAllVendors() {
        return this.vendorRepository.findAll()
                .stream()
                .map(
                        vendor -> VendorDTO.builder()
                                .name(vendor.getName())
                                .vendorUrl(VENDORS_BASE_URL + SEPARATOR + vendor.getId())
                                .build()
                ).collect(Collectors.toList());
    }

    @Override
    public VendorDTO findVendorById(Long id) {
        VendorDTO vendorDTO = this.vendorRepository
                .findById(id)
                .map(this.vendorMapper::vendorToVendorDTO)
                .orElseThrow(ResourceNotFoundException::new);
        vendorDTO.setVendorUrl(VENDORS_BASE_URL + SEPARATOR + id);
        return vendorDTO;
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
        return this.saveVendor(this.vendorMapper.vendorDTOToVendor(vendorDTO));
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
        Vendor vendor = this.vendorRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        vendor.setName(vendorDTO.getName());
        return this.saveVendor(vendor);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return this.vendorRepository.findById(id)
                .map(vendor -> {
                    if (vendorDTO.getName() != null) {
                        vendor.setName(vendorDTO.getName());
                    }
                    return this.saveVendor(vendor);
                }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendorById(Long id) {
        this.vendorRepository.deleteById(id);
    }


    //== private methods ==
    private VendorDTO saveVendor(Vendor vendor) {
        Vendor savedVendor = this.vendorRepository.save(vendor);
        VendorDTO vendorDTOToReturn = vendorMapper.vendorToVendorDTO(savedVendor);
        vendorDTOToReturn.setVendorUrl(VENDORS_BASE_URL + SEPARATOR + savedVendor.getId());
        return vendorDTOToReturn;
    }
}