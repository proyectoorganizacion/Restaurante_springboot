package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.EmpleadoRegisterRequest;
import com.restaurante.restaurante_spring.dto.request.UsuarioRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.UsuarioRegisterResponse;
import com.restaurante.restaurante_spring.entity.EmpleadoRestaurante;
import com.restaurante.restaurante_spring.entity.Restaurante;
import com.restaurante.restaurante_spring.entity.Rol;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.EmpleadoRestauranteRepository;
import com.restaurante.restaurante_spring.repository.RestauranteRepository;
import com.restaurante.restaurante_spring.repository.RolRepository;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import com.restaurante.restaurante_spring.service.UsuarioService;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final RestauranteRepository restauranteRepository;
    private final EmpleadoRestauranteRepository empleadoRestauranteRepository;

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

    @Override
    public UsuarioRegisterResponse registrarEmpleado(
            EmpleadoRegisterRequest request) {

        // 1. Obtener el usuario autenticado
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String correoPropietario =
                authentication.getName();

        // 2. Buscar al propietario en la base de datos
        Usuario propietario = usuarioRepository
                .findByCorreo(correoPropietario)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El propietario autenticado no existe"));

        // 3. Verificar que realmente tenga rol PROPIETARIO
        if (propietario.getRol() == null ||
                !"PROPIETARIO".equalsIgnoreCase(
                        propietario.getRol().getNombre())) {

            throw new RuntimeException(
                    "El usuario autenticado no es un propietario");
        }

        // 4. Buscar el restaurante del propietario
        Restaurante restaurante = restauranteRepository
                .findByIdPropietario(propietario.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "El propietario no tiene un restaurante registrado"));

        // 5. Validar que el idRol corresponda a EMPLEADO
        Rol rolEmpleado = rolRepository
                .findById(request.getIdRol())
                .orElseThrow(() ->
                        new RuntimeException(
                                "El rol especificado no existe"));

        if (!"EMPLEADO".equalsIgnoreCase(
                rolEmpleado.getNombre())) {

            throw new RuntimeException(
                    "El usuario debe tener el rol EMPLEADO");
        }

        // 6. Validar documento duplicado
        if (usuarioRepository.existsByDocIdentidad(
                request.getDocIdentidad())) {

            throw new RuntimeException(
                    "El documento ya está registrado");
        }

        // 7. Validar correo duplicado
        if (usuarioRepository.existsByCorreo(
                request.getCorreo())) {

            throw new RuntimeException(
                    "El correo ya está registrado");
        }

        // 8. Encriptar contraseña
        String passwordEncriptada =
                passwordEncoder.encode(
                        request.getPassword());

        // 9. Crear empleado
        Usuario empleado = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .docIdentidad(request.getDocIdentidad())
                .celular(request.getCelular())
                .correo(request.getCorreo())
                .password(passwordEncriptada)
                .rol(rolEmpleado)
                .build();

        Usuario empleadoGuardado =
                usuarioRepository.save(empleado);

        // 10. Crear relación empleado-restaurante
        EmpleadoRestaurante empleadoRestaurante =
                EmpleadoRestaurante.builder()
                        .usuario(empleadoGuardado)
                        .restaurante(restaurante)
                        .build();

        empleadoRestauranteRepository.save(
                empleadoRestaurante);

        // 11. Construir respuesta
        return UsuarioRegisterResponse.builder()
                .id(empleadoGuardado.getId())
                .nombre(empleadoGuardado.getNombre())
                .apellido(empleadoGuardado.getApellido())
                .docIdentidad(
                        empleadoGuardado.getDocIdentidad())
                .celular(empleadoGuardado.getCelular())
                .correo(empleadoGuardado.getCorreo())
                .rol(empleadoGuardado.getRol().getNombre())
                .mensaje("Empleado registrado correctamente")
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
