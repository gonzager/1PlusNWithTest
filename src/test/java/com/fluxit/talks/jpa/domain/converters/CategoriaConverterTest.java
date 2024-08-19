package com.fluxit.talks.jpa.domain.converters;

import com.fluxit.talks.jpa.domain.enumns.Catetoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CategoriaConverterTest {

    private CategoriaConverter categoriaConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoriaConverter = new CategoriaConverter();
    }

    @Test
    void testConvertToDatabaseColumn() {
        Catetoria catetoria = Catetoria.DEPORTE;
        String codigoCategoria = Catetoria.DEPORTE.getCodigo();

        String codigo = categoriaConverter.convertToDatabaseColumn(catetoria);
        assertEquals(codigoCategoria, codigo);
    }

    @Test
    void testConvertToDatabaseColumnNull() {
        String codigo = categoriaConverter.convertToDatabaseColumn(null);
        assertNull(codigo);
    }

    @Test
    void testConvertToEntityAttribute() {
        String codigo = Catetoria.MUSICA.getCodigo();
        Catetoria catetoria = categoriaConverter.convertToEntityAttribute(codigo);
        assertNotNull(catetoria);
        assertEquals(codigo, catetoria.getCodigo());
    }

    @Test
    void testConvertToEntityAttributeInvalidCode() {
        String invalidCode = "invalidCode";
        assertThrows(IllegalArgumentException.class, () -> categoriaConverter.convertToEntityAttribute(invalidCode));
    }

    @Test
    void testConvertToEntityAttributeNull() {
        Catetoria catetoria = categoriaConverter.convertToEntityAttribute(null);
        assertNull(catetoria);
    }
}
