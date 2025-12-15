package com.integration.crmerpsync.DTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CustomerBulkImport {
    @NotEmpty
    private List<@Valid CustomerCreate> customers;
}
