package com.course.jpaconherencia.servicice;

import com.course.jpaconherencia.domain.Bici;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiciService {
    Page<Bici> findAll(Pageable pageable);
    Bici findById(Long id);
}
