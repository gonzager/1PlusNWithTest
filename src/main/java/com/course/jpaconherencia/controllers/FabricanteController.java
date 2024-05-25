package com.course.jpaconherencia.controllers;

import com.course.jpaconherencia.domain.Fabricante;
import com.course.jpaconherencia.dtos.FabricanteCategoriaDTO;
import com.course.jpaconherencia.dtos.FabricanteDTO;
import com.course.jpaconherencia.dtos.ProductoSinFabricanteDTO;
import com.course.jpaconherencia.repositories.FabricanteRepository;
import com.course.jpaconherencia.servicice.impl.FabricanteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/fabricantes")
public class FabricanteController {
    @Autowired
    private FabricanteRepository fabricanteRepository;
    @Autowired
    private FabricanteServiceImpl fabricanteServiceImpl;

    @GetMapping("/{id}")
    public FabricanteDTO findAllWithProducto(@PathVariable Long id) {
        var fabricante = fabricanteRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Fabricante nao encontrado")
        );
        var fabricantesConProductos = fabricanteRepository.findAllWithProducto(id);
        var productosDelFabricante = fabricantesConProductos.stream().map(f ->
                new ProductoSinFabricanteDTO(
                        f.getProducto(),
                        f.getCodigo(),
                        f.getFecha(),
                        Period.between(f.getFecha().toLocalDate(), LocalDate.now()).getYears()
                )
        ).toList();
        return new FabricanteDTO(
                fabricante.getId(),
                fabricante.getNombre(),
                fabricante.getCategoria().name(),
                productosDelFabricante
        );
    }
}
