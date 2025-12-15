package com.integration.crmerpsync.DTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerCreate {

    @NotBlank @Size(max=150)
    private String name;

    @NotBlank @Email @Size(max=180)
    private String email;

    @Size(max=50)
    private String phone;
}
