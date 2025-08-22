package io.github.srdejo.technical.test.application.mappers;

import io.github.srdejo.technical.test.application.dtos.request.SignUpRequest;
import io.github.srdejo.technical.test.application.dtos.response.UserResponse;
import io.github.srdejo.technical.test.domain.entities.Phone;
import io.github.srdejo.technical.test.domain.entities.User;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static User toEntity(SignUpRequest request, String encodedPassword) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setCreated(java.time.LocalDateTime.now());
        user.setLastLogin(java.time.LocalDateTime.now());
        user.setActive(true);

        if (request.getPhones() != null) {
            List<Phone> phones = request.getPhones().stream()
                    .map(p -> {
                        Phone phone = new Phone();
                        phone.setNumber(p.getNumber());
                        phone.setCityCode(p.getCityCode());
                        phone.setCountryCode(p.getCountryCode());
                        phone.setUser(user);
                        return phone;
                    })
                    .collect(Collectors.toList());
            user.setPhones(phones);
        }

        return user;
    }

    public static UserResponse toResponse(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(token)
                .isActive(user.isActive())
                .build();
    }
}
