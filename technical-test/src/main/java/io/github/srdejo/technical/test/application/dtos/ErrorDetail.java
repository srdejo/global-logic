package io.github.srdejo.technical.test.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ErrorDetail {
    private LocalDateTime timestamp;
    private int codigo;
    private String detail;
}
