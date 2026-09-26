package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.UsuarioRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.UsuarioRegisterResponse;
import com.restaurante.restaurante_spring.entity.Rol;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.RolRepository;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import com.restaurante.restaurante_spring.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    @Override
    public UsuarioRegisterResponse registrarUsuario(
            UsuarioRegisterRequest request) {

        validarEdad(request.getFecha_nacimiento());

        if (usuarioRepository.existsByDocIdentidad(
                request.getDocIdentidad())) {

            throw new RuntimeException(
                    "El documento ya está registrado");
        }

        if (usuarioRepository.existsByCorreo(
                request.getCorreo())) {

            throw new RuntimeException(
                    "El correo ya está registrado");
        }

        Rol rolPropietario = rolRepository
                .findByNombre("PROPIETARIO")
                .orElseThrow(() -> new RuntimeException(
                        "El rol PROPIETARIO no existe"));

        String passwordEncriptada =
                passwordEncoder.encode(request.getPassword());

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .docIdentidad(request.getDocIdentidad())
                .celular(request.getCelular())
                .fecha_nacimiento(request.getFecha_nacimiento())
                .correo(request.getCorreo())
                .password(passwordEncriptada)
                .rol(rolPropietario)
                .build();

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        return UsuarioRegisterResponse.builder()
                .id(usuarioGuardado.getId())
                .nombre(usuarioGuardado.getNombre())
                .apellido(usuarioGuardado.getApellido())
                .docIdentidad(
                        usuarioGuardado.getDocIdentidad())
                .celular(usuarioGuardado.getCelular())
                .correo(usuarioGuardado.getCorreo())
                .rol(usuarioGuardado.getRol().getNombre())
                .mensaje("Usuario registrado correctamente")
                .build();
    }

    private void validarEdad(LocalDate fechaNacimiento) {

        if (fechaNacimiento == null) {
            throw new RuntimeException(
                    "La fecha de nacimiento es obligatoria");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new RuntimeException(
                    "La fecha de nacimiento no puede ser futura");
        }

        int edad = Period.between(
                fechaNacimiento,
                LocalDate.now()
        ).getYears();

        if (edad < 18) {
            throw new RuntimeException(
                    "El usuario debe ser mayor de edad");
        }
    }

}
