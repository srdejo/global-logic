package io.github.srdejo.technical.test.domain.service;

import io.github.srdejo.technical.test.application.dtos.request.LoginRequest;
import io.github.srdejo.technical.test.application.dtos.response.UserResponse;

public interface LoginService {
    UserResponse login(LoginRequest request);
    UserResponse loginWithToken(String token);
}
