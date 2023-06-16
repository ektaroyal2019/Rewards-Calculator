package com.ekta.telecom.RewardsCalculator.handler;

import com.ekta.telecom.RewardsCalculator.exception.ApiErrorResponse;
import com.ekta.telecom.RewardsCalculator.exception.RequestValidationErrorException;
import com.ekta.telecom.RewardsCalculator.exception.RewardException;
import com.ekta.telecom.RewardsCalculator.exception.TransactionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static String getRequestPath(final WebRequest webRequest) {
        return ((ServletWebRequest) webRequest).getRequest().getRequestURI();
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public final ResponseEntity<ApiErrorResponse> transactionNotFoundException(TransactionNotFoundException e, WebRequest webRequest) {
        log.error(prepareLogStatement(webRequest, e), e);
        return createResponse(HttpStatus.NOT_FOUND, webRequest, e);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Void> exception(Exception e, WebRequest webRequest) {
        log.error(prepareLogStatement(webRequest, e), e);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(RequestValidationErrorException.class)
    public ResponseEntity<ApiErrorResponse> validationErrorsException(RequestValidationErrorException e, WebRequest webRequest) {
        log.info(prepareLogStatement(webRequest, e));
        return createResponse(HttpStatus.BAD_REQUEST, webRequest, e);
    }

    public String prepareLogStatement(WebRequest webRequest, Exception e) {
        return MessageFormat.format("Error {0} occur due to {1} - for: {2}{3}", e.getClass().getSimpleName(), e.getMessage(),
                getRequestPath(webRequest), getWebRequestAttributesAsString(webRequest));
    }

    private Map<String, String> getWebRequestAttributes(WebRequest webRequest) {
        Map<String, String> parameterMap = new HashMap<>();

        final Object attribute = webRequest.getAttribute("org.springframework.web.servlet.View.pathVariables", 0);
        if (attribute != null) {
            parameterMap = new HashMap<>((Map<String, Object>) attribute).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> String.valueOf(v.getValue())));
        }
        return parameterMap;
    }

    private String getWebRequestAttributesAsString(WebRequest webRequest) {
        final Map<String, String> webRequestAttributes = getWebRequestAttributes(webRequest);
        final StringBuilder stringWebRequestAttributes = new StringBuilder();

        webRequestAttributes.keySet().forEach((String attributeKey) ->
        {
            final String attributeValue = String.valueOf(webRequestAttributes.get(attributeKey));
            stringWebRequestAttributes.append(", ")
                    .append(attributeKey)
                    .append(": ")
                    .append(attributeValue);
        });

        return stringWebRequestAttributes.toString();
    }

    private ResponseEntity<ApiErrorResponse> createResponse(HttpStatus httpStatus, WebRequest webRequest, RewardException e) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .httpStatusCode(httpStatus.value())
                .errorMessage(e.getMessage())
                .requestPath(getRequestPath(webRequest))
                .apiErrorMessage(e.getErrorMessage().getErrorCode())
                .resourceInformation(getWebRequestAttributes(webRequest))
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
