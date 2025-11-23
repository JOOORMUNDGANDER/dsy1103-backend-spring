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
public class CarritoDTO {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private List<ItemCarritoDTO> items;
    private Integer totalItems;
    private Double total;
}
