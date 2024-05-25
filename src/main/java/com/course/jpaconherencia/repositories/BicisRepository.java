package com.course.jpaconherencia.repositories;

import com.course.jpaconherencia.domain.Bici;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BicisRepository extends PagingAndSortingRepository<Bici, Long>, CrudRepository<Bici, Long> {
}
