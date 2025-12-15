package com.integration.crmerpsync.DTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.integration.crmerpsync.enums.OrderStatus;

@Data
public class ErpOrderStatusWebhook {

    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus status;

    private String reference;

}
