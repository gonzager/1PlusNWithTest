package com.course.jpaconherencia.servicice.impl;

import com.course.jpaconherencia.exceptions.NotFoundException;
import com.course.jpaconherencia.domain.Bici;
import com.course.jpaconherencia.repositories.BicisRepository;
import com.course.jpaconherencia.servicice.BiciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BiciServiceImpl implements BiciService {
    @Autowired
    BicisRepository bicisRepository;

    @Override
    public Page<Bici> findAll(Pageable pageable) {
        System.out.println(pageable);
        return bicisRepository.findAll(pageable);
    }

    @Override
    public Bici findById(Long id) {
        return bicisRepository.findById(id).orElseThrow(()->new NotFoundException("Bici no encontrada"));
    }
}
