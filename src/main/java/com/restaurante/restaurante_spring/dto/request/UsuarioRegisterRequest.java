package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import jakarta.validation.constraints.AssertTrue;
import java.time.Period;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegisterRequest {
//validaciones
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    @Pattern(
            regexp = "\\d+",
            message = "El documento debe contener únicamente números"
    )
    private String documentoDeIdentidad;

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

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @AssertTrue(message = "El usuario debe ser mayor de 18 años")
    public boolean esMayorDeEdad() {

        if (fechaNacimiento == null ||
                fechaNacimiento.isAfter(LocalDate.now())) {
            return true;
        }

        return Period.between(
                fechaNacimiento,
                LocalDate.now()
        ).getYears() >= 18;
    }

    @AssertTrue(message = "La fecha de nacimiento no puede ser en el futuro")
    public boolean fechaNoEsFutura() {

        if (fechaNacimiento == null) {
            return true;
        }

        return !fechaNacimiento.isAfter(LocalDate.now());
    }

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;
}