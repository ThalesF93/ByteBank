package br.com.bytebank.customers.openfeign.feignclients;


import br.com.bytebank.customers.openfeign.config.FeignConfig;
import br.com.bytebank.customers.openfeign.dtos.requests.AccountRequestDTO;
import br.com.bytebank.customers.openfeign.fallback.AccountClienteFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service",
        url = "http://localhost:8082",
        path = "api/v1/accounts",
        fallback = AccountClienteFallback.class,
        configuration = FeignConfig.class)

public interface AccountClient {

    @PostMapping
    ResponseEntity<Void> openAccount(@RequestBody AccountRequestDTO request);



}
