package com.fluxit.talks.jpa.controllers;

import com.fluxit.talks.jpa.domain.Fabricante;
import com.fluxit.talks.jpa.domain.enumns.Catetoria;
import com.fluxit.talks.jpa.services.impl.FabricanteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@DisplayName("Template Controller Test")
@AutoConfigureMockMvc

class FabricanteControllerTest {

    @Autowired
    private FabricanteServiceImpl fabricanteServiceImpl;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        fabricanteServiceImpl.deletaAll();
    }

    @Test
    void findAllWithProductoNotFoundExepcion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/fabricantes/123")).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    void findAllWithProducto() throws Exception {

        //Arrange
        var fabricante = fabricanteServiceImpl.save( new Fabricante(0L, "Fabricante1", Catetoria.MUSICA));

        mockMvc.perform(MockMvcRequestBuilders.get("/fabricantes/" + fabricante.getId())).andExpect(
                MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(fabricante.getId().toString()))
                .andExpect(jsonPath("$.fabricante").value(fabricante.getNombre()))
                .andExpect(jsonPath("$.categoria").value("MUSICA"))
                .andExpect(jsonPath("$.productos").isEmpty());
    }

    @Test
    void findAllWithProductoFindAll() throws Exception {

        //Arrange
        var fabricante = fabricanteServiceImpl.save( new Fabricante(0L, "Fabricante1", Catetoria.MUSICA));

        mockMvc.perform(MockMvcRequestBuilders.get("/fabricantes")).andExpect(
                        MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
        ;
    }
}