//package br.com.bytebank.accounts.infrastructure.openfeign.fallback;
//
//import br.com.bytebank.accounts.domain.exception.ServiceUnavailableException;
//import br.com.bytebank.accounts.infrastructure.feignclient.CustomerClient;
//import br.com.bytebank.accounts.infrastructure.openfeign.dto.response.CustomerClientResponseDTO;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//public class CustomerClientFallback implements CustomerClient {
//
//    @Override
//    public CustomerClientResponseDTO findCustomerById(UUID id) {
//        throw new ServiceUnavailableException("Customer service unavailable");
//    }
//}
