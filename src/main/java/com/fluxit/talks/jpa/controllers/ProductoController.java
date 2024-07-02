package com.fluxit.talks.jpa.controllers;

import com.fluxit.talks.jpa.domain.Producto;
import com.fluxit.talks.jpa.dtos.FabricanteCategoriaDTO;
import com.fluxit.talks.jpa.dtos.ProductoDTO;
import com.fluxit.talks.jpa.dtos.ProductoSinFabricanteDTO;
import com.fluxit.talks.jpa.exceptions.NotFoundException;
import com.fluxit.talks.jpa.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

;    @Autowired
    ProductoService productoService;

    @GetMapping
    public Page<ProductoDTO> findAll(@PageableDefault(value = 5000) Pageable pageable) {
        Page<Producto> productos = productoService.findAllWithFabricante(pageable);
        List<ProductoDTO> productosDTO = productos.getContent().stream().map(p -> new ProductoDTO(
                p.getCodigo(),
                p.getNombre(),
                p.getAntiguedad(),
                p.getFabricantes().stream()
                        .map(f -> new FabricanteCategoriaDTO(f.getId(),f.getNombre(), f.getCategoria().name()))
                        .collect(Collectors.toSet())
        )).toList();
        return new PageImpl<>(productosDTO, pageable, productos.getTotalElements());
    }

    @GetMapping("/{id}")
    public ProductoSinFabricanteDTO ProductoDTOfindById(@PathVariable Long id) {
        var producto = productoService.findById(id).orElseThrow( () -> new NotFoundException("Producto not found"));

        return new ProductoSinFabricanteDTO(producto.getNombre(), producto.getCodigo(), producto.getFechaLanzamiento(), producto.getAntiguedad());
    }
}
