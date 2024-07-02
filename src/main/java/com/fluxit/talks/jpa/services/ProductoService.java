package com.fluxit.talks.jpa.services;

import com.fluxit.talks.jpa.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductoService {
    Optional<Producto> findById(Long id);
    Page<Producto> findAllWithFabricante(Pageable pageable);
}
