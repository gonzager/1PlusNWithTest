package com.course.jpaconherencia.servicice;

import com.course.jpaconherencia.dtos.IFabricanteDTO;

import java.util.List;

public interface FabricanteService {
    List<IFabricanteDTO> findAllWithProducto(Long id);
}
