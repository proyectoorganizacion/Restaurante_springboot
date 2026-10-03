package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.AdminRegisterRequest;
import com.restaurante.restaurante_spring.dto.request.LoginRequest;
import com.restaurante.restaurante_spring.dto.response.LoginResponse;
import com.restaurante.restaurante_spring.entity.Rol;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.RolRepository;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import com.restaurante.restaurante_spring.security.JwtService;
import com.restaurante.restaurante_spring.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Usuario usuario = usuarioRepository
                .findByCorreo(loginRequest.getCorreo())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        boolean claveCorrecta = passwordEncoder.matches(
                loginRequest.getPassword(),
                usuario.getPassword()
        );

        if (!claveCorrecta) {
            throw new RuntimeException("Clave incorrecta");
        }

        String token = jwtService.generateToken(
                usuario.getCorreo(),
                usuario.getRol()
        );

        return LoginResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void registrarAdministrador(AdminRegisterRequest request) {

        boolean existeAdministrador = usuarioRepository.findAll()
                .stream()
                .anyMatch(usuario ->
                        usuario.getRol() != null &&
                                "ADMINISTRADOR".equalsIgnoreCase(
                                        usuario.getRol().getNombre()
                                )
                );

        if (existeAdministrador) {
            throw new RuntimeException(
                    "Ya existe un administrador registrado"
            );
        }

        if (usuarioRepository.existsByDocIdentidad(
                request.getDocIdentidad())) {

            throw new RuntimeException(
                    "Ya existe un usuario registrado con ese documento"
            );
        }

        if (usuarioRepository.existsByCorreo(
                request.getCorreo())) {

            throw new RuntimeException(
                    "Ya existe un usuario registrado con ese correo"
            );
        }

        Rol rolAdministrador = rolRepository
                .findByNombre("ADMINISTRADOR")
                .orElseThrow(() ->
                        new RuntimeException(
                                "El rol ADMINISTRADOR no existe"
                        )
                );

        Usuario administrador = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .docIdentidad(request.getDocIdentidad())
                .celular(request.getCelular())
                .fecha_nacimiento(
                        LocalDate.parse(
                                request.getFecha_nacimiento()
                        )
                )
                .correo(request.getCorreo())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .rol(rolAdministrador)
                .build();

        usuarioRepository.save(administrador);
    }
}