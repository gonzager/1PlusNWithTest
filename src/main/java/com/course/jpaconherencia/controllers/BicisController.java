package com.course.jpaconherencia.controllers;


import com.course.jpaconherencia.dtos.BiciDTO;

import com.course.jpaconherencia.servicice.BiciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/bicis")
public class BicisController {

    @Autowired
    BiciService biciService;

    @GetMapping
    Page<BiciDTO> getAllBicis(@PageableDefault(value = 2, page = 0) Pageable pageable) {
        return biciService.findAll(pageable).map( bici ->
                BiciDTO.builder()
                        .modelo(bici.getDescripcion())
                        .rodado(bici.getRodado())
                        .build()
        );
    }
    @GetMapping("/{id}")
    BiciDTO getBiciById(@PathVariable Long id) {
        var bici = biciService.findById(id);
        return BiciDTO.builder()
                .modelo(bici.getDescripcion())
                .rodado(bici.getRodado())
                .build();
    }
}
