package com.level.up.proyecto.spring.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoDTO {

    @NotBlank(message = "La dirección de envío es requerida")
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String direccionEnvio;

    @Size(max = 500, message = "Las notas no pueden exceder 500 caracteres")
    private String notas;
}
