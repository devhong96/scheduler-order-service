package com.scheduler.orderservice.order.client.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.orderservice.infra.exception.custom.AuthorityException;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class OrderFeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        String errorMessage = getErrorMessage(response);

        return switch (response.status()) {
            case 400 -> new IllegalArgumentException("Bad Request: " + errorMessage);
            case 403 -> new AuthorityException("Forbidden: " + errorMessage);
            case 404 -> new NotFoundException("Not Found: " + errorMessage);
            case 405 -> new IllegalAccessException();
            default -> new Exception(response.reason() + errorMessage);
        };
    }

    private String getErrorMessage(Response response) {
        if (response.body() == null) {
            return "Response body is null. Reason: " + response.reason();
        }
        try (InputStream bodyIs = response.body().asInputStream()) {

            Map<String, Object> errorBody = objectMapper.readValue(bodyIs, Map.class);

            return errorBody.getOrDefault("message", "Could not parse error message from body").toString();
        } catch (IOException e) {
            log.error("Failed to read error response body", e);
            return "Failed to decode error body. Reason: " + response.reason();
        }
    }
}
