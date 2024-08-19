package com.fluxit.talks.jpa.services.impl;

import com.fluxit.talks.jpa.domain.Fabricante;
import com.fluxit.talks.jpa.dtos.IFabricanteDTO;
import com.fluxit.talks.jpa.repositories.FabricanteRepository;
import com.fluxit.talks.jpa.services.FabricanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FabricanteServiceImpl implements FabricanteService {

    @Autowired
    private FabricanteRepository fabricanteRepository;
    @Override
    public List<IFabricanteDTO> findAllWithProducto(Long id) {
        return fabricanteRepository.findAllWithProducto(id);
    }

    @Override
    public List<Fabricante> findAll() {
        return fabricanteRepository.findAll();
    }

    @Override
    public Optional<Fabricante> findById(Long id) {
        return fabricanteRepository.findById(id);
    }

    @Override
    public Fabricante save(Fabricante fabricante){
        return fabricanteRepository.save(fabricante);
    }

    @Override
    public void deletaAll() {
        fabricanteRepository.deleteAll();
    }
}
