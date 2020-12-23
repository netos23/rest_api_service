package ru.fbtw.navigator.rest_api_service.controller;

import org.springframework.web.bind.annotation.*;
import ru.fbtw.navigator.rest_api_service.domain.Project;
import ru.fbtw.navigator.rest_api_service.response.BaseResponse;
import ru.fbtw.navigator.rest_api_service.security.JwtUtil;
import ru.fbtw.navigator.rest_api_service.response.Response;
import ru.fbtw.navigator.rest_api_service.service.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final UpdateService updateService;
    private final ProjectService projectService;

    public RestApiController(
            UpdateService updateService,
            ProjectService projectService
    ) {
        this.updateService = updateService;
        this.projectService = projectService;
    }

    @RequestMapping("/publish_project")
    public Response publish(
            @RequestBody String jsonBody
    ) {
        if (updateService.publish(jsonBody)) {
            return new Response("success", 200);
        }

        return new Response("Project exist or auth field", 409);
    }

    @PostMapping("/create_project")
    public BaseResponse createProject(
            @RequestHeader(name = JwtUtil.AUTHORIZATION) String auth,
            @RequestBody Project project
    ) {
        if (projectService.addProject(project, auth)) {
            return new Response("success", 200);
        }

        return new Response("Project exist or auth field", 409);
    }

    @PostMapping("/update_project")
    public BaseResponse updateProject(@RequestBody Project project) {
        if (projectService.updateProject(project)) {
            return new Response("success", 200);
        }

        return new Response("Project exist or auth field", 409);
    }


    @PostMapping("/remove_project")
    public BaseResponse removeProject(@RequestBody Project project) {
        if (projectService.removeProject(project)) {
            return new Response("success", 200);
        }

        return new Response("Project exist or auth field", 409);
    }

    @GetMapping("/project_list")
    public Object projectList(@RequestHeader(name = JwtUtil.AUTHORIZATION) String auth) {
        List<Project> projects = projectService.getProjectList(auth);
        if (projects != null) {
            return projects;
        }
        return new Response("User not found", 403);
    }


}
