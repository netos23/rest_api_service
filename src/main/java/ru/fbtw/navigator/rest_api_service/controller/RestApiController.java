package ru.fbtw.navigator.rest_api_service.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.fbtw.navigator.rest_api_service.service.RedirectService;
import ru.fbtw.navigator.rest_api_service.service.ResponseService;
import ru.fbtw.navigator.rest_api_service.service.ResponseType;
import ru.fbtw.navigator.rest_api_service.service.UpdateService;

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

    @RequestMapping("/update")
    public String updateMap(
            @RequestParam String apiKey,
            @RequestBody String jsonBody
    ) {
        String userId = "";
        boolean successUpdate = parserService.updateMap(userId,jsonBody,false);

       return "true";
    }

}
