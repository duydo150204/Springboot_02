package com.example.springboot_02;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// "test" profile = also load application-test.properties, which points the app at an
// in-memory H2 database instead of XAMPP.
@SpringBootTest
@ActiveProfiles("test")
class Springboot02ApplicationTests {

    @Test
    void contextLoads() {
    }

}
