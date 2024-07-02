package com.fluxit.talks.jpa.services.impl;

import com.fluxit.talks.jpa.domain.Producto;
import com.fluxit.talks.jpa.repositories.ProductoRepository;
import com.fluxit.talks.jpa.services.ProductoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional(Transactional.TxType.NEVER)
    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    @Transactional(Transactional.TxType.NEVER)
    public Page<Producto> findAllWithFabricante(Pageable pageable) {
        return productoRepository.findAllWithFabricantes(pageable);
    }
}
