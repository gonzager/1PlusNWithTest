package com.fluxit.talks.jpa.controllers;

import com.fluxit.talks.jpa.dtos.FabricanteCategoriaDTO;
import com.fluxit.talks.jpa.dtos.FabricanteDTO;
import com.fluxit.talks.jpa.dtos.ProductoSinFabricanteDTO;
import com.fluxit.talks.jpa.exceptions.NotFoundException;
import com.fluxit.talks.jpa.services.impl.FabricanteServiceImpl;
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
    private FabricanteServiceImpl fabricanteServiceImpl;

    @GetMapping
    public List<FabricanteCategoriaDTO> findAll(){
        return fabricanteServiceImpl.findAll().stream().map( f ->
                new FabricanteCategoriaDTO(
                        f.getId(),
                        f.getNombre(),
                        f.getCategoria().toString()
                ) ).toList();
    }

    @GetMapping("/{id}")
    public FabricanteDTO findAllWithProducto(@PathVariable Long id) {
        var fabricante = fabricanteServiceImpl.findById(id).orElseThrow(
                () -> new NotFoundException("Fabricante no encontrado")
        );
        var fabricantesConProductos = fabricanteServiceImpl.findAllWithProducto(id);
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
