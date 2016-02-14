package com.server.http;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.server.controller.TopicController;
import com.server.controller.UserController;
import com.server.http.login.TokenValidatorFactory;
import com.server.model.Topic;
import com.server.model.User;

public class UserResource extends Resource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

	private static final Gson gson = new Gson();
	

	@Inject
	private TokenValidatorFactory tokenValidatorFactory;
	@Inject
	private UserController userController;
	@Inject
	private TopicController topicController;
	
	public void registerResource() {
		post("/user/update", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");

				String token = jsonRequest.getString("token");
				String loginService = jsonRequest.getString("loginService");
				String topicsRaw = jsonRequest.getString("topics");
				
				String userId = getUserId(token, loginService);
				User user = userController.getById(userId);
				List<String> topicsList = Arrays.asList(topicsRaw.split(" "));
				List<Topic> topics = topicController.findOrPersist(topicsList);
				
				user.setTopics(topics);
				userController.persist(user);
				
				jsonResponseMessage.put("message", "ok");

			} catch (Exception e) {
				logger.error("Error in request /add", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});

		get("/user/get/:userid/:loginService", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String userId = request.params(":userid");
				String loginService = request.params(":loginService");

				User user = userController.getByExternalUserId(userId, loginService);
				jsonResponseMessage.put("user", new JSONObject(gson.toJson(user)));

			} catch (Exception e) {
				logger.error("Error in request /get", e);
				jsonResponseMessage.put("user", "null");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
		
		get("/user/get/withtoken/:token/:loginService", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String token = request.params(":userid");
				String loginService = request.params(":loginService");
				
				String userId = getUserId(token, loginService);
				User user = userController.getById(Long.valueOf(userId));
				jsonResponseMessage.put("user", new JSONObject(gson.toJson(user)));

			} catch (Exception e) {
				logger.error("Error in request /get", e);
				jsonResponseMessage.put("user", "null");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
		
		post("/user/check", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				
				String name = jsonRequest.getString("name");
				String extUserId = jsonRequest.getString("extUserId");
				String token = jsonRequest.getString("token");
				String loginService = jsonRequest.getString("loginService");
				String client = jsonRequest.getString("client");

				logger.info("check user: " + name + " userId: " + extUserId + " token: " + token + " service " + loginService + " client " + client);
				
				if(tokenValidatorFactory.get(loginService, token, extUserId).validate()) {
					User user = userController.getByExternalUserId(extUserId, loginService);
					if(user != null) {
						jsonResponseMessage.put("message", "ok");
						tokenRegister(user.getId(), extUserId, loginService, token);
					}else {
						user = userController.add(new User(extUserId, loginService, name));
						tokenRegister(user.getId(), extUserId, loginService, token);
						jsonResponseMessage.put("message", "firstlogin");
					}
				}else {
					jsonResponseMessage.put("message", "error");
				}

			} catch (Exception e) {
				logger.error("Error in request /add", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
	}
}
