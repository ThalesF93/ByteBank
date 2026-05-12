package br.com.bytebank.customers.infrastructure.feignclient;


import br.com.bytebank.customers.infrastructure.config.FeignConfig;
import br.com.bytebank.customers.api.dtos.client.requests.AccountRequestDTO;
import br.com.bytebank.customers.infrastructure.feignclient.fallback.AccountClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bytebank-accounts",
        path = "api/v1/accounts",
        fallback = AccountClientFallback.class,
        configuration = FeignConfig.class)

public interface AccountClient {

    @PostMapping
    ResponseEntity<Void> openAccount(@RequestBody AccountRequestDTO request);



}
