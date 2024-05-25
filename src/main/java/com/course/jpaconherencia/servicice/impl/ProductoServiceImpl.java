package com.course.jpaconherencia.servicice.impl;

import com.course.jpaconherencia.domain.Producto;
import com.course.jpaconherencia.repositories.ProductoRepository;
import com.course.jpaconherencia.servicice.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Override
    public Page<Producto> findAll(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    public Page<Producto> findAllWithFabricante(Pageable pageable) {
        return productoRepository.findAllWithFabricantes(pageable);
    }
}
