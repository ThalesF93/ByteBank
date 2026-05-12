package br.com.bytebank.accounts.infrastructure.config;



import br.com.bytebank.accounts.domain.exception.ResourceNotFoundException;
import br.com.bytebank.accounts.domain.exception.ServiceUnavailableException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.coyote.BadRequestException;



public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()){
            case 400 -> new BadRequestException("Invalid Request");
            case 404 -> new ResourceNotFoundException("Resource not found");
            case 503 -> new ServiceUnavailableException("Service Unavailable");
            default -> new FeignException.FeignClientException(
                    response.status(), response.reason(), response.request(), null, null
            );
        };
    }
}
