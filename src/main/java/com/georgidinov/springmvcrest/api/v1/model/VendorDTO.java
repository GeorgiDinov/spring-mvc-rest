package com.georgidinov.springmvcrest.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDTO {

    private String name;
    private String vendorUrl;

}