package ru.fbtw.navigator.rest_api_service.controller;

import org.springframework.web.bind.annotation.*;
import ru.fbtw.navigator.rest_api_service.security.AuthRequest;
import ru.fbtw.navigator.rest_api_service.security.AuthResponse;
import ru.fbtw.navigator.rest_api_service.service.*;

@RestController
@RequestMapping("/api")
public class RestApiController {

    private final UpdateService parserService;
    private final RedirectService redirectService;
    private final ResponseService responseService;

    public RestApiController(UpdateService parserService,
                             RedirectService redirectService, ResponseService responseService) {
        this.parserService = parserService;
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

    @RequestMapping("/create_project")
    public String createProject() {
        return "";
    }

    @RequestMapping("/remove_project")
    public String removeProject() {
        return "";
    }



}
