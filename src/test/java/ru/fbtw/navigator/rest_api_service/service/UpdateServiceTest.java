package ru.fbtw.navigator.rest_api_service.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UpdateServiceTest {

    @Autowired
    UpdateService updateService;
    String res;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        File file = new File("test_res/default_env_1.json");
        res = new Scanner(file).nextLine();

    }

    @Test
    void updateMap() {
        updateService.updateMap("test_user",res,true);
    }
}