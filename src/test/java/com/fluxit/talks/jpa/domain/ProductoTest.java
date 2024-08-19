package com.fluxit.talks.jpa.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
    }

    @Test
    void testGetAntiguedad_whenLanzadoHoy() {
        // Fecha de lanzamiento es hoy
        producto.setFechaLanzamiento(Date.valueOf(LocalDate.now()));
        assertEquals(0, producto.getAntiguedad());
    }

    @Test
    void testGetAntiguedad_whenLanzadoHaceUnAno() {
        // Fecha de lanzamiento hace exactamente un año
        producto.setFechaLanzamiento(Date.valueOf(LocalDate.now().minusYears(1)));
        assertEquals(1, producto.getAntiguedad());
    }

    @Test
    void testGetAntiguedad_whenLanzadoHaceCasiUnAno() {
        // Fecha de lanzamiento hace casi un año (364 días atrás)
        producto.setFechaLanzamiento(Date.valueOf(LocalDate.now().minusDays(364)));
        assertEquals(0, producto.getAntiguedad());
    }

    @Test
    void testGetAntiguedad_whenLanzadoHaceMasDeUnAno() {
        // Fecha de lanzamiento hace más de un año (366 días atrás)
        producto.setFechaLanzamiento(Date.valueOf(LocalDate.now().minusDays(366)));
        assertEquals(1, producto.getAntiguedad());
    }

    @Test
    void testGetAntiguedad_whenLanzadoHaceExactamenteDosAnios() {
        // Fecha de lanzamiento hace exactamente dos años
        producto.setFechaLanzamiento(Date.valueOf(LocalDate.now().minusYears(2)));
        assertEquals(2, producto.getAntiguedad());
    }

    @Test
    void testGetAntiguedad_whenLanzadoEnAnoBisiesto() {
        // Fecha de lanzamiento en un año bisiesto (ej. 29 de febrero)
        producto.setFechaLanzamiento(Date.valueOf(LocalDate.of(2020, 2, 29)));
        LocalDate now = LocalDate.now();

        if (now.isLeapYear() || now.getDayOfYear() >= 60) {
            assertEquals(now.getYear() - 2020, producto.getAntiguedad());
        } else {
            assertEquals(now.getYear() - 2020 - 1, producto.getAntiguedad());
        }
    }
}
