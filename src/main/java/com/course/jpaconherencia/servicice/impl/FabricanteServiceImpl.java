package com.course.jpaconherencia.servicice.impl;

import com.course.jpaconherencia.dtos.IFabricanteDTO;
import com.course.jpaconherencia.repositories.FabricanteRepository;
import com.course.jpaconherencia.servicice.FabricanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FabricanteServiceImpl implements FabricanteService {

    @Autowired
    private FabricanteRepository fabricanteRepository;
    @Override
    public List<IFabricanteDTO> findAllWithProducto(Long id) {
        return fabricanteRepository.findAllWithProducto(id);
    }
}
