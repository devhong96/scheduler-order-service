package com.scheduler.orderservice.order.client.error;

import com.scheduler.orderservice.infra.exception.custom.AuthorityException;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Component;

@Component
public class EbookFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        return switch (response.status()) {
            case 400 -> new IllegalArgumentException();
            case 403 -> new AuthorityException();
            case 404 -> new NotFoundException();
            case 405 -> new IllegalAccessException();
            default -> new Exception(response.reason());
        };
    }
}
