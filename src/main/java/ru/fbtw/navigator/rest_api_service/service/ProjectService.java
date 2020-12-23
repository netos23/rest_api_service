package ru.fbtw.navigator.rest_api_service.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.domain.ExecutedProject;
import ru.fbtw.navigator.rest_api_service.domain.Platform;
import ru.fbtw.navigator.rest_api_service.domain.Project;
import ru.fbtw.navigator.rest_api_service.domain.User;
import ru.fbtw.navigator.rest_api_service.exseption.MissingPlatformException;
import ru.fbtw.navigator.rest_api_service.repository.ExecutedPlatformsRepo;
import ru.fbtw.navigator.rest_api_service.repository.ProjectRepo;
import ru.fbtw.navigator.rest_api_service.repository.UserRepo;
import ru.fbtw.navigator.rest_api_service.security.JwtProvider;
import ru.fbtw.navigator.rest_api_service.security.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {

	private final ProjectRepo projectRepo;
	private final ExecutedPlatformsRepo platformsRepo;
	private final UserRepo userRepo;
	private final JwtProvider provider;

	public ProjectService(
			ProjectRepo projectRepo,
			ExecutedPlatformsRepo platformsRepo,
			UserRepo userRepo,
			JwtProvider provider
	) {
		this.projectRepo = projectRepo;
		this.platformsRepo = platformsRepo;
		this.userRepo = userRepo;
		this.provider = provider;
	}

	public boolean addProject(Project project, String auth) {
		User user = getUser(auth);

		if (user != null) {
			project.setOwner(user);
			projectRepo.save(project);

			initPlatforms(project);
			return true;
		}

		return false;
	}


	public boolean updateProject(Project project) {
		if (project.getId() == null) {
			return false;
		}

		Optional<Project> oldProject = projectRepo.findById(project.getId());

		if (oldProject.isPresent()) {
			Project newProject = oldProject.get();

			newProject.setName(project.getName());
			newProject.setPlatforms(project.getPlatforms());

			newProject.setTelegramApiKey(project.getTelegramApiKey());
			newProject.setTelegramName(project.getTelegramName());

			newProject.setAppName(project.getAppName());
			newProject.setUserPackage(project.getUserPackage());

			newProject.setBody(project.getBody());
			updatePlatforms(newProject, project);

			projectRepo.save(newProject);
			return true;
		}

		return false;
	}

	private void initPlatforms(Project project) {
		for (Platform platform : project.getPlatforms()) {
			ExecutedProject executedProject = initPlatform(project, platform);
			platformsRepo.save(executedProject);
		}
	}


	private ExecutedProject initPlatform(Project project, Platform platform) {
		ExecutedProject executedProject = new ExecutedProject();

		executedProject.setProject(project);
		executedProject.setActive(false);
		executedProject.setPlatforms(platform);
		return executedProject;
	}

	public void updatePlatforms(Project old, Project cur) {

		for (Platform platform : old.getPlatforms()) {
			if (!cur.getPlatforms().contains(platform)) {
				removePlatform(old, platform);
			}
		}

		for (Platform platform : cur.getPlatforms()) {
			if (!old.getPlatforms().contains(platform)) {
				invokePlatform(old, platform);
			}
		}
	}

	@SneakyThrows
	public void activatePlatform(Project project, Platform platform){
		Optional<ExecutedProject> executedProject
				= platformsRepo.findByProjectAndPlatforms(project, platform);

		if(executedProject.isPresent()){
			executedProject.get().setActive(true);
			platformsRepo.save(executedProject.get());
		}else{
			throw new MissingPlatformException();
		}
	}

	private void invokePlatform(Project project, Platform platform) {
		Optional<ExecutedProject> executedProjectOptional = platformsRepo.findByProjectAndPlatforms(project, platform);
		if (executedProjectOptional.isPresent()) {
			log.error("Platform: {}, exist for project: {}", platform, project);
		} else {
			initPlatform(project, platform);
			log.info("Platform: {}, inited for project: {}", platform, project);
		}
	}

	private void removePlatform(Project project, Platform platform) {
		Optional<ExecutedProject> executedProjectOptional = platformsRepo.findByProjectAndPlatforms(project, platform);
		if (executedProjectOptional.isPresent()) {
			platformsRepo.delete(executedProjectOptional.get());
			log.info("Platform: {} removed for project: {}", platform, project);
			//todo: остановить сервисы
		} else {
			log.error("Platform: {} is not present for project: {}", platform, project);
		}
	}

	public boolean isPlatformInited(Project project, Platform platform) {
		Set<ExecutedProject> platforms = platformsRepo.findAllByProject(project);
		Optional<ExecutedProject> executedProject = platforms.stream()
				.filter(proj -> proj.getPlatforms().equals(platform))
				.findFirst();
		return executedProject.isPresent() && executedProject.get().getActive();

	}

	public List<Project> getProjectList(String auth) {
		User user = getUser(auth);
		if (user != null) {
			return getPreparedProjects(user);
		}
		return null;
	}


	private List<Project> getPreparedProjects(User user) {
		return user.getProjects().stream()
				.map(this::prepareProject)
				.collect(Collectors.toList());
	}

	private Project prepareProject(Project project) {
		Project copyProject = new Project();

		copyProject.setId(project.getId());

		copyProject.setName(project.getName());
		copyProject.setPlatforms(project.getPlatforms());

		copyProject.setTelegramName(project.getTelegramName());
		copyProject.setTelegramApiKey(project.getTelegramApiKey());

		copyProject.setAppName(project.getAppName());
		copyProject.setUserPackage(project.getUserPackage());

		copyProject.setBody(project.getBody());

		return copyProject;
	}

	private User getUser(String auth) {
		String token = JwtUtil.getTokenFromHeader(auth);
		String username = provider.getLoginFromToken(token);
		return userRepo.findUserByUsername(username);
	}

	public boolean removeProject(Project project) {
		Optional<Project> oldProject = projectRepo.findById(project.getId());

		if (oldProject.isPresent()) {

			Set<Platform> platforms = project.getPlatforms();
			for(Platform platform : platforms){
				removePlatform(oldProject.get(),platform);
			}

			projectRepo.deleteById(project.getId());
			return true;
		}
		//todo: удалить платформы
		return false;
	}


	public Optional<Project> getProjectById(Long id) {
		return projectRepo.findById(id);
	}
}
