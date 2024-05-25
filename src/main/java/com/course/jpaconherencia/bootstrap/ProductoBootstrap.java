package com.course.jpaconherencia.bootstrap;

import com.course.jpaconherencia.domain.Fabricante;
import com.course.jpaconherencia.domain.Producto;
import com.course.jpaconherencia.domain.enumns.Catetoria;
import com.course.jpaconherencia.repositories.FabricanteRepository;
import com.course.jpaconherencia.repositories.ProductoRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;


@Service
public class ProductoBootstrap implements InitializingBean {

    final static int __BATCH_SIZE__ = 500;
    final static int __BATCH_LOOP__ = 30;
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
        Random random = new Random();
        return array[random.nextInt(array.length)];
    }

    private Date randomDate() {
        Random random = new Random();
        int year = random.nextInt(2023 - 1970) + 1970; // Entre 1970 y 2022
        int month = random.nextInt(12) + 1; // Entre 1 y 12
        int day = random.nextInt(Month.of(month).length(Year.isLeap(year))) + 1;// Días válidos para el mes

        return  Date.valueOf(LocalDate.of(year, month, day));
    }

    private void crearFabricantesTemplate(int desde, int hasta) {

        IntStream.range(desde, hasta + 1).forEach(i -> {
            with( new Fabricante(), f-> {
                f.setNombre(String.format("Fabricante->%d", i));
                f.setCategoria(anyOne(Catetoria.values()));
                fabricantes.add(f);
            });
        });

    }

    private void crearProductoTemplate(int desde, int hasta){
        IntStream.range(desde, hasta + 1).forEach(i -> {
            with( new Producto(), p-> {
                p.setNombre(String.format("Producto->%d", i));
                p.setCodigo(UUID.randomUUID());
                p.setDisponible(true);
                p.setFechaLanzamiento(randomDate());
                var fabricantesPorProducto = new HashSet<Fabricante>();
                IntStream.range(1, new Random().nextInt(5) + 1).forEach(
                        n -> fabricantesPorProducto.add(anyOne(fabricantes.toArray(new Fabricante[0])))
                );
                p.setFabricantes(fabricantesPorProducto);
                productos.add(p);
            });
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        crearFabricantesTemplate(1, __BATCH_SIZE__);
        fabricanteRepository.saveAll(fabricantes);
        IntStream.range(0,__BATCH_LOOP__).forEach(i->
                crearProductoTemplate(i * __BATCH_SIZE__ + 1, (i + 1) * __BATCH_SIZE__)
        );
        productoRepository.saveAll(productos);
    }
}
