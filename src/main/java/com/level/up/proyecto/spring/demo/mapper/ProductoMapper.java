package com.level.up.proyecto.spring.demo.mapper;

import com.level.up.proyecto.spring.demo.dto.ProductoDTO;
import com.level.up.proyecto.spring.demo.dto.ProductoCrearDTO;
import com.level.up.proyecto.spring.demo.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    // Convertir Producto a ProductoDTO
    public ProductoDTO aDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        Integer porcentajeDescuento = calcularPorcentajeDescuento(
                producto.getPrecioOriginal(),
                producto.getPrecio()
        );

        return ProductoDTO.builder()
                .id(producto.getId())
                .codigo(producto.getCodigo())
                .categoria(producto.getCategoria())
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .precioOriginal(producto.getPrecioOriginal())
                .imagen(producto.getImagen())
                .descripcion(producto.getDescripcion())
                .descripcionProducto(producto.getDescripcionProducto())
                .oferta(producto.getOferta())
                .especificaciones(producto.getEspecificaciones())
                .stock(producto.getStock())
                .activo(producto.getActivo())
                .porcentajeDescuento(porcentajeDescuento)
                .build();
    }

    // Convertir ProductoCrearDTO a Producto
    public Producto aEntidad(ProductoCrearDTO dto) {
        if (dto == null) {
            return null;
        }

        return Producto.builder()
                .codigo(dto.getCodigo())
                .categoria(dto.getCategoria())
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .precioOriginal(dto.getPrecioOriginal())
                .imagen(dto.getImagen())
                .descripcion(dto.getDescripcion())
                .descripcionProducto(dto.getDescripcionProducto())
                .oferta(dto.getOferta() != null ? dto.getOferta() : false)
                .especificaciones(dto.getEspecificaciones())
                .stock(dto.getStock() != null ? dto.getStock() : 0)
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    // Actualizar entidad existente con datos del DTO
    public void actualizarEntidad(Producto producto, ProductoCrearDTO dto) {
        if (dto.getCodigo() != null) {
            producto.setCodigo(dto.getCodigo());
        }
        if (dto.getCategoria() != null) {
            producto.setCategoria(dto.getCategoria());
        }
        if (dto.getNombre() != null) {
            producto.setNombre(dto.getNombre());
        }
        if (dto.getPrecio() != null) {
            producto.setPrecio(dto.getPrecio());
        }
        if (dto.getPrecioOriginal() != null) {
            producto.setPrecioOriginal(dto.getPrecioOriginal());
        }
        if (dto.getImagen() != null) {
            producto.setImagen(dto.getImagen());
        }
        if (dto.getDescripcion() != null) {
            producto.setDescripcion(dto.getDescripcion());
        }
        if (dto.getDescripcionProducto() != null) {
            producto.setDescripcionProducto(dto.getDescripcionProducto());
        }
        if (dto.getOferta() != null) {
            producto.setOferta(dto.getOferta());
        }
        if (dto.getEspecificaciones() != null) {
            producto.setEspecificaciones(dto.getEspecificaciones());
        }
        if (dto.getStock() != null) {
            producto.setStock(dto.getStock());
        }
        if (dto.getActivo() != null) {
            producto.setActivo(dto.getActivo());
        }
    }

    // Calcular porcentaje de descuento
    private Integer calcularPorcentajeDescuento(Double precioOriginal, Double precioActual) {
        if (precioOriginal == null || precioOriginal <= 0 || precioActual == null) {
            return null;
        }
        if (precioOriginal <= precioActual) {
            return 0;
        }
        double descuento = ((precioOriginal - precioActual) / precioOriginal) * 100;
        return (int) Math.round(descuento);
    }
}
