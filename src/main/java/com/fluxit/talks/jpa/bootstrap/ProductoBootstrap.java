package com.fluxit.talks.jpa.bootstrap;

import com.fluxit.talks.jpa.domain.Fabricante;
import com.fluxit.talks.jpa.domain.Producto;
import com.fluxit.talks.jpa.domain.enumns.Catetoria;
import com.fluxit.talks.jpa.repositories.FabricanteRepository;
import com.fluxit.talks.jpa.repositories.ProductoRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;


@Service
public class ProductoBootstrap implements InitializingBean {

    @Value("${data.initialization.enabled:false}")
    private boolean isDataInitializationEnabled;

    static final int BATCH_SIZE = 500;
    static final int BATCH_LOOP = 30;
    static final Random random = new SecureRandom();
    final FabricanteRepository fabricanteRepository;
    final ProductoRepository productoRepository;

    Set<Fabricante> fabricantes = new HashSet<>();
    Set<Producto> productos = new HashSet<>();


    public ProductoBootstrap(FabricanteRepository fabricanteRepository, ProductoRepository productoRepository) {
        this.fabricanteRepository = fabricanteRepository;
        this.productoRepository = productoRepository;
    }

    public static <T> void with(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
    }

    public static <T> T anyOne(T[] array) {
        return array[random.nextInt(array.length)];
    }

    private Date randomDate() {
        int year = random.nextInt(2023 - 1970) + 1970; // Entre 1970 y 2022
        int month = random.nextInt(12) + 1; // Entre 1 y 12
        int day = random.nextInt(Month.of(month).length(Year.isLeap(year))) + 1;// Días válidos para el mes
        return  Date.valueOf(LocalDate.of(year, month, day));
    }

    private void crearFabricantesTemplate(int desde, int hasta) {

        IntStream.range(desde, hasta + 1).forEach(i ->
            with( new Fabricante(), f-> {
                f.setNombre(String.format("Fabricante->%d", i));
                f.setCategoria(anyOne(Catetoria.values()));
                fabricantes.add(f);
            })
        );

    }

    private void crearProductoTemplate(int desde, int hasta){
        IntStream.range(desde, hasta + 1).forEach(i ->
            with( new Producto(), p-> {
                p.setNombre(String.format("Producto->%d", i));
                p.setCodigo(UUID.randomUUID());
                p.setDisponible(true);
                p.setFechaLanzamiento(randomDate());
                var fabricantesPorProducto = new HashSet<Fabricante>();
                IntStream.range(1, random.nextInt(10) + 1).forEach(
                        n -> fabricantesPorProducto.add(anyOne(fabricantes.toArray(new Fabricante[0])))
                );
                p.setFabricantes(fabricantesPorProducto);
                productos.add(p);
            })
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!isDataInitializationEnabled) return;
        crearFabricantesTemplate(1, BATCH_SIZE);
        fabricanteRepository.saveAll(fabricantes);
        IntStream.range(0,BATCH_LOOP).forEach(i->
                crearProductoTemplate(i * BATCH_SIZE + 1, (i + 1) * BATCH_SIZE)
        );
        productoRepository.saveAll(productos);
    }
}
