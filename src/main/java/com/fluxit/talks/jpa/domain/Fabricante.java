package com.fluxit.talks.jpa.domain;

import com.fluxit.talks.jpa.domain.enumns.Catetoria;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Fabricante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique=true)
    String nombre;

    Catetoria categoria;

}
