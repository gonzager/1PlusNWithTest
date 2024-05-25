package com.course.jpaconherencia.repositories;

import com.course.jpaconherencia.domain.Fabricante;
import com.course.jpaconherencia.dtos.IFabricanteDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FabricanteRepository extends CrudRepository<Fabricante, Long> {

    @Query(
            "SELECT f.id as id " +
            ", f.nombre as fabricante " +
            ", f.categoria as categoria " +
            ", p.nombre as producto " +
            ", p.codigo as codigo " +
            ", p.fechaLanzamiento as fecha " +
            "From Producto p INNER JOIN p.fabricantes f " +
                    "WHERE f.id =:id"
    )
    List<IFabricanteDTO> findAllWithProducto(Long id);


}

