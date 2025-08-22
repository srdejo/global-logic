package io.github.srdejo.technical.test.domain.service;

import io.github.srdejo.technical.test.application.dtos.request.SignUpRequest;
import io.github.srdejo.technical.test.application.dtos.response.UserResponse;

import javax.validation.Valid;

public interface SignUpService {
    UserResponse register(@Valid SignUpRequest request);
}
