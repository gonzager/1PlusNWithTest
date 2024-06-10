package com.fluxit.talks.jpa.services;

import com.fluxit.talks.jpa.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {
    Page<Producto> findAll(Pageable pageable);
    Page<Producto> findAllWithFabricante(Pageable pageable);
}
