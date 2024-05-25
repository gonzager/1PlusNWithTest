package com.course.jpaconherencia.servicice;

import com.course.jpaconherencia.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoService {
    Page<Producto> findAll(Pageable pageable);
    Page<Producto> findAllWithFabricante(Pageable pageable);
}
