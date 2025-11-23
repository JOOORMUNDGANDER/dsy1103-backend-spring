package com.level.up.proyecto.spring.demo.controller;

import com.level.up.proyecto.spring.demo.dto.JwtRespuestaDTO;
import com.level.up.proyecto.spring.demo.dto.LoginDTO;
import com.level.up.proyecto.spring.demo.dto.MensajeRespuestaDTO;
import com.level.up.proyecto.spring.demo.dto.RegistroDTO;
import com.level.up.proyecto.spring.demo.model.Usuario;
import com.level.up.proyecto.spring.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST: Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<JwtRespuestaDTO> iniciarSesion(@Valid @RequestBody LoginDTO loginDTO) {
        JwtRespuestaDTO respuesta = authService.iniciarSesion(loginDTO);
        return ResponseEntity.ok(respuesta);
    }

    // POST: Registrar nuevo usuario
    @PostMapping("/registro")
    public ResponseEntity<MensajeRespuestaDTO> registrar(@Valid @RequestBody RegistroDTO registroDTO) {
        Usuario usuario = authService.registrar(registroDTO);
        return new ResponseEntity<>(
                new MensajeRespuestaDTO("Usuario registrado exitosamente: " + usuario.getNombreUsuario()),
                HttpStatus.CREATED);
    }
}
