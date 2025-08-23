package io.github.srdejo.technical.test.application.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El correo debe tener formato válido")
    @Pattern(regexp = ".+@.+\\..+", message = "El correo debe tener formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
            regexp = "^(?=(?:[^0-9]*[0-9]){2}[^0-9]*$)(?=.*[A-Z])[A-Za-z0-9]{8,12}$",
            message = "La contraseña no cumple con el formato requerido"
    )
    private String password;

    private List<PhoneRequest> phones;
}
