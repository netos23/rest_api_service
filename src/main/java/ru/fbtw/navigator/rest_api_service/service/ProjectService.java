package ru.fbtw.navigator.rest_api_service.service;

import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.domain.Project;
import ru.fbtw.navigator.rest_api_service.domain.User;
import ru.fbtw.navigator.rest_api_service.repository.ProjectRepo;
import ru.fbtw.navigator.rest_api_service.repository.UserRepo;
import ru.fbtw.navigator.rest_api_service.security.JwtProvider;
import ru.fbtw.navigator.rest_api_service.security.JwtUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final JwtProvider provider;

    public ProjectService(ProjectRepo projectRepo, UserRepo userRepo, JwtProvider provider) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.provider = provider;
    }

    public boolean addProject(Project project, String auth) {
        User user = getUser(auth);

        if (user != null) {
            project.setOwner(user);
            projectRepo.save(project);
            return true;
        }

        return false;
    }

    public boolean updateProject(Project project){
        if(project.getId() == null){
            return false;
        }

        Optional<Project> oldProject = projectRepo.findById(project.getId());

        if(oldProject.isPresent()){
            Project newProject = oldProject.get();

            newProject.setName(project.getName());
            newProject.setPlatforms(project.getPlatforms());

            newProject.setTelegramApiKey(project.getTelegramApiKey());
            newProject.setTelegramName(project.getTelegramName());

            newProject.setAppName(project.getAppName());
            newProject.setUserPackage(project.getUserPackage());

            projectRepo.save(newProject);
            //todo: обновить платформы
            return true;
        }

        return false;
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

        if(oldProject.isPresent()){
            projectRepo.deleteById(project.getId());
            return true;
        }
        //todo: удалить платформы
        return false;
    }
}
