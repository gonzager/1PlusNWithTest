package com.course.jpaconherencia.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    UUID codigo;

    @Column(nullable = false)
    String nombre;

    @Column(columnDefinition = "boolean default true")
    Boolean disponible;

    @Column(nullable = false)
    Date fechaLanzamiento;

    @Transient
    public Integer getAntiguedad() {
        return Period.between(this.fechaLanzamiento.toLocalDate(), LocalDate.now()).getYears();
    }

    @ManyToMany(fetch = FetchType.LAZY)
    Set<Fabricante> fabricantes;


}
