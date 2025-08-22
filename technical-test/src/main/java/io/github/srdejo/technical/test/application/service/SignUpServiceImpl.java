package io.github.srdejo.technical.test.application.service;

import io.github.srdejo.technical.test.application.dtos.request.SignUpRequest;
import io.github.srdejo.technical.test.application.dtos.response.UserResponse;
import io.github.srdejo.technical.test.application.mappers.UserMapper;
import io.github.srdejo.technical.test.domain.entities.User;
import io.github.srdejo.technical.test.domain.exceptions.UserAlreadyExistsException;
import io.github.srdejo.technical.test.domain.service.SignUpService;
import io.github.srdejo.technical.test.infrastructure.repositories.UserRepository;
import io.github.srdejo.technical.test.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(SignUpRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistsException("User already exists");
        });

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = UserMapper.toEntity(request, encodedPassword);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return UserMapper.toResponse(user, token);
    }
}
