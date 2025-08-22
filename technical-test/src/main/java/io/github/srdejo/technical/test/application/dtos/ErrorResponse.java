package io.github.srdejo.technical.test.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private List<ErrorDetail> error;
}
