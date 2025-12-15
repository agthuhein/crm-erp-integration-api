package com.integration.crmerpsync.controller;

import com.integration.crmerpsync.DTO.CustomerBulkImport;
import com.integration.crmerpsync.DTO.ErpOrderStatusWebhook;
import com.integration.crmerpsync.entity.Order;
import com.integration.crmerpsync.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/integrations")
public class IntegrationController {
    private final IntegrationService integrationService;

    @PostMapping("/import/customers")
    public Map<String, Object> bulkImport(@Valid @RequestBody CustomerBulkImport importData) {
        return integrationService.bulkImportCustomers(importData);
    }

    @PostMapping("/webhooks/erp-order-status")
    public Order webhook(@Valid @RequestBody ErpOrderStatusWebhook erpOrderStatusWebhook) {
        return integrationService.erpOrderStatusWebHook(erpOrderStatusWebhook);
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "UP", "service", "crm-erp-sync-api");
    }

    @GetMapping("/metrics")
    public Map<String, Object> metrics() {
        return integrationService.metrics();
    }

    @GetMapping("/export/orders.csv")
    public ResponseEntity<String> exportCsv(@RequestParam Instant from,
                                            @RequestParam Instant to) {
        String csv = integrationService.exportOrdersCsv(from, to);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.csv")
                .contentType(MediaType.valueOf("text/csv"))
                .body(csv);
    }
}
