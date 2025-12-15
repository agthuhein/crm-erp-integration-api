package com.integration.crmerpsync.DTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class OrderCreate {
    @NotNull
    private Long customerId;

    @NotNull
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {
        @NotBlank
        private String sku;

        @NotNull @Min(1)
        private Integer quantity;
    }
}
