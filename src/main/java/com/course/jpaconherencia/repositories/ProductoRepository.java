package com.course.jpaconherencia.repositories;

import com.course.jpaconherencia.domain.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends PagingAndSortingRepository<Producto, Long>, CrudRepository<Producto, Long> {
    @EntityGraph(attributePaths = {"fabricantes"})
    @Query("Select p from Producto p")
    Page<Producto> findAllWithFabricantes(Pageable pageable);
    Page<Producto> findAll(Pageable pageable);
}
