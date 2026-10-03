package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoRegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    @Pattern(
            regexp = "\\d+",
            message = "El documento debe contener únicamente números"
    )
    private String docIdentidad;

    @NotBlank(message = "El celular es obligatorio")
    @Size(
            max = 13,
            message = "El celular no puede superar los 13 caracteres"
    )
    @Pattern(
            regexp = "\\+?\\d+",
            message = "El celular solo puede contener números y el símbolo +"
    )
    private String celular;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotNull(message = "El idRol es obligatorio")
    private Integer idRol;

    @NotBlank(message = "La clave es obligatoria")
    private String password;
}