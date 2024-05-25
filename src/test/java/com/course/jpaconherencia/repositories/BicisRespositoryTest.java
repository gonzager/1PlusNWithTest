package com.course.jpaconherencia.repositories;

import com.course.jpaconherencia.domain.Bici;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Bicis Respository Test")
public class BicisRespositoryTest {
    @Autowired BicisRepository bicisRepository;

    @Test
    void findAllisEmpty() {
        //Arrange
        Pageable pageable = PageRequest.of(0, 10);
        //Act
        var bicis = bicisRepository.findAll(pageable);
        //assert
        assertTrue(bicis.getContent().isEmpty());
    }

    @Test
    void findAllisNotEmpty() {
        //Arrange
        var bici = new Bici(0L,"Playera", 20);
        Pageable pageable = PageRequest.of(0, 10);
        bicisRepository.save(bici);

        //Act
        var bicis = bicisRepository.findAll(pageable);

        //assert
        assertFalse(bicis.getContent().isEmpty());
    }

    @Test
    void count() {
        var cantBicis = bicisRepository.count();
        assertEquals(0, cantBicis);
    }

}
