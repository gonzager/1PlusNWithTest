package com.course.jpaconherencia.dtos;

import java.util.Set;
import java.util.UUID;

public record ProductoDTO (UUID codigo , String nombre, Integer antiguedad, Set<FabricanteCategoriaDTO> fabricantes) {}
