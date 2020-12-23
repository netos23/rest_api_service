package ru.fbtw.navigator.rest_api_service.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.fbtw.navigator.rest_api_service.domain.Platform;
import ru.fbtw.navigator.rest_api_service.domain.Project;

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
		Project project = new Project();
		project.setId(32243432423L);
		project.getPlatforms().add(Platform.APP);
		updateService.updateMap(project, res);
	}
}