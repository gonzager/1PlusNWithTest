package com.fluxit.talks.jpa.dtos;

import java.sql.Date;
import java.util.UUID;

public record ProductoSinFabricanteDTO(String nombre, UUID codigo ,  Date fecha_lanzamiento, Integer antiguedad) { }
