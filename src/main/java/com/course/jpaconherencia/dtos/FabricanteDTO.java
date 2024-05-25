package com.course.jpaconherencia.dtos;

import java.util.List;

public record FabricanteDTO(Long id, String fabricante, String categoria, List<ProductoSinFabricanteDTO> productos) { }
