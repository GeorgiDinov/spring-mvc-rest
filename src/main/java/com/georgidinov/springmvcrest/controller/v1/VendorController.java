package com.georgidinov.springmvcrest.controller.v1;

import com.georgidinov.springmvcrest.api.v1.model.VendorDTO;
import com.georgidinov.springmvcrest.api.v1.model.VendorListDTO;
import com.georgidinov.springmvcrest.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.georgidinov.springmvcrest.util.ApplicationConstants.VENDORS_BASE_URL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(VENDORS_BASE_URL)
public class VendorController {

    //== constants ==
    private final String CLASS_NAME = this.getClass().getSimpleName();

    //== fields ==
    private final VendorService vendorService;


    @Operation(summary = "This Operation Retrieves All Vendors Stored In DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieve All Vendors From DB",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "NOT Found",
                    content = @Content)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorListDTO getAllVendors() {
        log.info("getAllVendors() called in {}", CLASS_NAME);
        return VendorListDTO.builder().vendorDTOS(this.vendorService.getAllVendors()).build();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO getVendorById(@PathVariable String id) {
        log.info("getVendorById() called in {}", CLASS_NAME);
        return this.vendorService.findVendorById(Long.valueOf(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO createNewVendor(@RequestBody VendorDTO vendorDTO) {
        log.info("createNewVendor() called in {}", CLASS_NAME);
        return this.vendorService.createNewVendor(vendorDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO updateVendor(@PathVariable String id, @RequestBody VendorDTO vendorDTO) {
        log.info("updateVendor() called in {}", CLASS_NAME);
        return this.vendorService.updateVendor(Long.valueOf(id), vendorDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO patchVendor(@PathVariable String id, @RequestBody VendorDTO vendorDTO) {
        log.info("patchVendor() called in {}", CLASS_NAME);
        return this.vendorService.patchVendor(Long.valueOf(id), vendorDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendor(@PathVariable String id) {
        log.info("deleteVendor() called in {}", CLASS_NAME);
        this.vendorService.deleteVendorById(Long.valueOf(id));
    }

}