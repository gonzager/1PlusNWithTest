package com.fluxit.talks.jpa.dtos;

import java.sql.Date;
import java.util.UUID;

public interface IFabricanteDTO {
    Long getId();
    String getFabricante();
    String getCategoria();
    String getProducto();
    UUID getCodigo();
    Date getFecha();
}
