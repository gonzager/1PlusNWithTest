package com.fluxit.talks.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
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
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    Set<Fabricante> fabricantes;

}
