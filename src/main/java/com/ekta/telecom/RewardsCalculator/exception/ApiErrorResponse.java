package com.ekta.telecom.RewardsCalculator.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {
    @JsonProperty(value = "apiErrorMessage", required = true)
    private String apiErrorMessage;

    @JsonProperty(value = "httpStatusCode", required = true)
    private int httpStatusCode;

    @JsonProperty(value = "requestPath", required = true)
    private String requestPath;

    @JsonProperty("resourceInformation")
    private Map<String, String> resourceInformation;

    @JsonProperty(value = "errorMessage", required = true)
    private String errorMessage;
}
