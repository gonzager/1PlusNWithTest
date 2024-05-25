package com.course.jpaconherencia.controllers;

import com.course.jpaconherencia.domain.Producto;
import com.course.jpaconherencia.dtos.FabricanteCategoriaDTO;
import com.course.jpaconherencia.dtos.ProductoDTO;
import com.course.jpaconherencia.servicice.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping
    public Page<ProductoDTO> findAll(@PageableDefault(value = 5000) Pageable pageable) {
        Page<Producto> productos = productoService.findAllWithFabricante(pageable);
        List<ProductoDTO> productosDTO = productos.getContent().stream().map(p -> new ProductoDTO(
                p.getCodigo(),
                p.getNombre(),
                p.getAntiguedad(),
                p.getFabricantes().stream()
                        .map(f -> new FabricanteCategoriaDTO(f.getNombre(), f.getCategoria().name()))
                        .collect(Collectors.toSet())
        )).toList();
        return new PageImpl<>(productosDTO, pageable, productos.getTotalElements());
    }
}
