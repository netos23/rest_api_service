package ru.fbtw.navigator.rest_api_service.controller;

import org.springframework.web.bind.annotation.*;
import ru.fbtw.navigator.rest_api_service.domain.Project;
import ru.fbtw.navigator.rest_api_service.response.BaseResponse;
import ru.fbtw.navigator.rest_api_service.security.JwtUtil;
import ru.fbtw.navigator.rest_api_service.response.Response;
import ru.fbtw.navigator.rest_api_service.service.*;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final UpdateService parserService;
    private final ProjectService projectService;
    private final RedirectService redirectService;
    private final ResponseService responseService;

    public RestApiController(
            UpdateService parserService,
            ProjectService projectService,
            RedirectService redirectService,
            ResponseService responseService
    ) {
        this.parserService = parserService;
        this.projectService = projectService;
        this.redirectService = redirectService;
        this.responseService = responseService;
    }

    @RequestMapping("/update_project")
    public String updateMap(
            @RequestParam String apiKey,
            @RequestBody String jsonBody
    ) {
        String userId = "";
        boolean successUpdate = parserService.updateMap(userId, jsonBody, false);

        return "true";
    }

    @PostMapping("/create_project")
    public BaseResponse createProject(
            @RequestHeader(name = JwtUtil.AUTHORIZATION) String auth,
            @RequestBody Project project
    ) {
       if(projectService.addProject(project,auth)){
            return new Response("success",200);
       }

       return new Response("Project exist or auth field",409);
    }

    @RequestMapping("/remove_project")
    public String removeProject() {
        return "";
    }


}
