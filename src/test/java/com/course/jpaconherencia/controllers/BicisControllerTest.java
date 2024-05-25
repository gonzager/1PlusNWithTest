package com.course.jpaconherencia.controllers;

import com.course.jpaconherencia.exceptions.NotFoundException;
import com.course.jpaconherencia.servicice.BiciService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Bicis Controller Test")
class BicisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private BiciService biciService;


    @Test
    void whenGetAllBicis() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bicis"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void whenGetOneBiciAndNotFoundIt() throws Exception {
        Mockito.doThrow(NotFoundException.class).when(biciService).findById(Mockito.anyLong());
        mockMvc.perform(MockMvcRequestBuilders.get("/bicis/0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}
