package ru.fbtw.navigator.rest_api_service.service;

import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.domain.Platform;
import ru.fbtw.navigator.rest_api_service.domain.Project;

import java.util.Optional;

@Slf4j
@Service
public class UpdateService {

	private final FireBaseService fireBaseService;
	private final TelegramService telegramService;
	private final ProjectService projectService;

	public UpdateService(
			FireBaseService fireBaseService,
			TelegramService telegramService,
			ProjectService projectService
	) {
		this.fireBaseService = fireBaseService;
		this.telegramService = telegramService;
		this.projectService = projectService;
	}

	public boolean updateMap(Project project, String jsonBody) {
		String projectId = project.getId().toString();

		if (project.hasPlatform(Platform.APP)) {
			boolean isNewFirebase = projectService.isPlatformInited(project, Platform.APP);
			fireBaseService.updateData(projectId, jsonBody, isNewFirebase);
			projectService.activatePlatform(project,Platform.APP);
		}

		if (project.hasPlatform(Platform.TG_BOT)) {
			try {
				boolean isNewTelegram = true;
				telegramService.updateData(projectId, jsonBody, isNewTelegram);
				projectService.activatePlatform(project,Platform.TG_BOT);
			}catch (Exception e){
				log.error("Error while updating bot for project: {}",projectId);
			}
		}
		// todo: пробросить исключения
		return true;
	}

	public boolean publish(String jsonBody) {
		Long projectId = JsonParser.parseString(jsonBody)
				.getAsJsonObject()
				.get("id")
				.getAsLong();

		Optional<Project> project = projectService.getProjectById(projectId);
		if (project.isPresent()) {
			updateMap(project.get(), jsonBody);
		} else {
			log.error("Wrong project id");
			return false;
		}

		return false;
	}
}
