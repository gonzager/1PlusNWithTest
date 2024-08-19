package com.fluxit.talks.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;


import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Profile("test")
class JpaNMas1QueriesApplicationTest {
    @Test
    void contextLoads() {
        assertTrue(true);
    }
}



