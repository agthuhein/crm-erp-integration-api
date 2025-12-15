package com.integration.crmerpsync.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerUpdate {
    @NotBlank @Size(max=150)
    private String name;

    @Size(max=50)
    private String phone;
}
