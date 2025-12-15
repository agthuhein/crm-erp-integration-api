package com.integration.crmerpsync.DTO;

import com.integration.crmerpsync.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdate {
    @NotNull
    private OrderStatus status;
}
