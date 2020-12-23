package ru.fbtw.navigator.rest_api_service.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.fbtw.navigator.rest_api_service.domain.TelegramServer;
import ru.fbtw.navigator.rest_api_service.exseption.TelegramBotCreateException;
import ru.fbtw.navigator.rest_api_service.exseption.TelegramBotException;
import ru.fbtw.navigator.rest_api_service.repository.TelegramBotRepo;
import ru.fbtw.navigator.rest_api_service.repository.TelegramServerRepo;
import ru.fbtw.navigator.rest_api_service.response.Response;
import ru.fbtw.navigator.rest_api_service.security.JwtProvider;
import ru.fbtw.navigator.rest_api_service.security.JwtUtil;

import java.io.IOException;
import java.util.Objects;

@Service
public class TelegramService implements PlatformService {
	private static final String CREATE = "/create";

	private final String serviceToken;
	private final JwtProvider jwtProvider;

	private final ProjectService projectService;
	private final TelegramBotRepo telegramBotRepo;
	private final TelegramServerRepo telegramServerRepo;
	private final OkHttpClient client;

	public TelegramService(
			@Value("${service.login}") String serviceUsername,
			JwtProvider jwtProvider,
			ProjectService projectService,
			TelegramBotRepo telegramBotRepo,
			TelegramServerRepo telegramServerRepo
	) {
		this.jwtProvider = jwtProvider;
		this.telegramBotRepo = telegramBotRepo;
		this.telegramServerRepo = telegramServerRepo;
		this.serviceToken = jwtProvider.generateToken(serviceUsername);
		this.projectService = projectService;

		client = new OkHttpClient().newBuilder()
				.build();
	}

	@SneakyThrows
	@Override
	public void updateData(String projectId, String json, boolean isNew) {
		if (isNew) {
			createBot(json);
		} else {
			updateBot(projectId, json);
		}
	}

	private void updateBot(String projectId, String json) {

	}


	private void createBot(String json) throws TelegramBotException, IOException {
		TelegramServer targetServer = getNextServer();
		sendRequest(targetServer.getUrl() + CREATE, json);
	}

	private TelegramServer getNextServer() throws TelegramBotException {
		TelegramServer targetServer = null;
		Iterable<TelegramServer> servers = telegramServerRepo.findAll();

		int slotCount = 0;

		for (TelegramServer server : servers) {
			slotCount = server.getEmptySlotsCount();
			if (slotCount > 0) {
				targetServer = server;
				break;
			}
		}

		if (targetServer == null) {
			throw new TelegramBotException("All telegram slots exists");
		}

		slotCount--;
		targetServer.setEmptySlotsCount(slotCount);
		telegramServerRepo.save(targetServer);

		return targetServer;
	}

	private void removeBot(String projectId) {

	}

	private void sendRequest(String url, String body) throws IOException,
			TelegramBotCreateException, NullPointerException, JsonSyntaxException {
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody requestBody = RequestBody.create(body, mediaType);

		Request request = new Request.Builder()
				.url(url)
				.method("POST", requestBody)
				.addHeader("Content-Type", "application/json")
				.addHeader(JwtUtil.AUTHORIZATION, "Bearer " + serviceToken)
				.build();

		ResponseBody responseBody = client.newCall(request).execute().body();
		Objects.requireNonNull(responseBody);

		Response response = parseResponseBody(responseBody);

		if (!isResponseSuccess(response)) {
			throw new TelegramBotCreateException("Error while creating bot: " + response.getMessage());
		}
	}

	private Response parseResponseBody(ResponseBody body) throws IOException {
		JsonObject jsonObject = JsonParser.parseString(body.string())
				.getAsJsonObject();

		String message = jsonObject.get("message").getAsString();
		int code = jsonObject.get("code").getAsInt();

		return new Response(message, code);
	}

	private boolean isResponseSuccess(Response response) {
		return response.getStatus() == 200;
	}


}
