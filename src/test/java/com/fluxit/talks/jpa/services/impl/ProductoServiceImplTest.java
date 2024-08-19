package com.fluxit.talks.jpa.services.impl;

import com.fluxit.talks.jpa.domain.Producto;
import com.fluxit.talks.jpa.repositories.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testFindById() {
        Long id = 12345L;
        Producto producto = new Producto();
        producto.setId(id);
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(producto, result.get());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        Long id = 54321L;
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Producto> result = productoService.findById(id);

        assertFalse(result.isPresent());
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void testFindAllWithFabricante() {
        Pageable pageable = PageRequest.of(0, 10);
        Producto producto = new Producto();
        Page<Producto> page = new PageImpl<>(Collections.singletonList(producto));
        when(productoRepository.findAllWithFabricantes(pageable)).thenReturn(page);

        Page<Producto> result = productoService.findAllWithFabricante(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productoRepository, times(1)).findAllWithFabricantes(pageable);
    }

    @Test
    void testFindAllWithFabricanteEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Producto> emptyPage = Page.empty();
        when(productoRepository.findAllWithFabricantes(pageable)).thenReturn(emptyPage);

        Page<Producto> result = productoService.findAllWithFabricante(pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(productoRepository, times(1)).findAllWithFabricantes(pageable);
    }
}
