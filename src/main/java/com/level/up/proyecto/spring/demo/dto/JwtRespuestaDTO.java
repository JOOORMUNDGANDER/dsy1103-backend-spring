package com.level.up.proyecto.spring.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRespuestaDTO {

    private String token;
    private String tipo;
    private Long id;
    private String nombreUsuario;
    private String email;
    private List<String> roles;

    public JwtRespuestaDTO(String token, Long id, String nombreUsuario, String email, List<String> roles) {
        this.token = token;
        this.tipo = "Bearer";
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.roles = roles;
    }
}
