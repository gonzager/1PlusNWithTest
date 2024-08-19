package com.fluxit.talks.jpa.services;

import com.fluxit.talks.jpa.domain.Fabricante;
import com.fluxit.talks.jpa.dtos.IFabricanteDTO;

import java.util.List;
import java.util.Optional;

public interface FabricanteService {
    List<IFabricanteDTO> findAllWithProducto(Long id);
    List<Fabricante> findAll();
    Optional<Fabricante> findById(Long id);
    Fabricante save(Fabricante fabricante);
    void deletaAll();
}
