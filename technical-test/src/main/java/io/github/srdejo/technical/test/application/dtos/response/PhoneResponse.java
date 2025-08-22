package io.github.srdejo.technical.test.application.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PhoneResponse {
    private Long number;
    @JsonProperty("citycode")
    private Integer cityCode;
    @JsonProperty("contrycode")
    private String countryCode;
}
