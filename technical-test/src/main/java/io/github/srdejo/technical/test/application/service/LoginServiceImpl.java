package io.github.srdejo.technical.test.application.service;

import io.github.srdejo.technical.test.application.dtos.request.LoginRequest;
import io.github.srdejo.technical.test.application.dtos.response.UserResponse;
import io.github.srdejo.technical.test.application.mappers.UserMapper;
import io.github.srdejo.technical.test.domain.entities.User;
import io.github.srdejo.technical.test.domain.service.LoginService;
import io.github.srdejo.technical.test.infrastructure.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return getUserResponse(user);
    }

    @Override
    public UserResponse loginWithToken(String token) {
        String email = jwtService.extractEmail(token);
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return getUserResponse(user);
    }

    private UserResponse getUserResponse(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String newToken = jwtService.generateToken(user.getEmail());

        return UserMapper.toResponse(user, newToken);
    }
}
