package ru.fbtw.navigator.rest_api_service.service;

import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.domain.Project;
import ru.fbtw.navigator.rest_api_service.domain.User;
import ru.fbtw.navigator.rest_api_service.repository.ProjectRepo;
import ru.fbtw.navigator.rest_api_service.repository.UserRepo;
import ru.fbtw.navigator.rest_api_service.security.JwtProvider;
import ru.fbtw.navigator.rest_api_service.security.JwtUtil;

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

    public boolean addProject(Project project, String auth){
        String token = JwtUtil.getTokenFromHeader(auth);
        String username = provider.getLoginFromToken(token);
        User user = userRepo.findUserByUsername(username);

        if(user != null){
            project.setOwner(user);
            projectRepo.save(project);
            return true;
        }

        return false;
    }
}
