package com.course.jpaconherencia.domain.converters;

import com.course.jpaconherencia.domain.enumns.Catetoria;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class CategoriaConverter implements AttributeConverter<Catetoria, String> {
    @Override
    public String convertToDatabaseColumn(Catetoria catetoria) {
        if (catetoria == null) {
            return null;
        }
        return catetoria.getCodigo();
    }

    @Override
    public Catetoria convertToEntityAttribute(String codigo) {
        if (codigo == null) {
            return null;
        }
        return Stream.of(Catetoria.values())
                .filter(c->c.getCodigo().equals(codigo))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
