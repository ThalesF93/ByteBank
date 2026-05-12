package br.com.bytebank.accounts.infrastructure.feignclient;

import br.com.bytebank.accounts.infrastructure.config.FeignConfig;
import br.com.bytebank.accounts.api.dtos.client.response.CustomerClientResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        configuration = FeignConfig.class,
       // fallback = CustomerClientFallback.class,
        name = "bytebank-customer",
        path = "/api/v2/customers"
)
public interface CustomerClient {

    @GetMapping("/{id}")
    CustomerClientResponseDTO findCustomerById(@PathVariable UUID id);

}
