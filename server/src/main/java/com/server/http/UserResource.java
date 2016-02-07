package com.server.http;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jboss.weld.context.RequestContext;
import org.jboss.weld.context.unbound.UnboundLiteral;
import org.jboss.weld.environment.se.WeldContainer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.server.controller.TopicController;
import com.server.controller.UserController;
import com.server.http.login.TokenValidatorFactory;
import com.server.model.Topic;
import com.server.model.User;

public class UserResource {

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
				
				String userId = jsonRequest.getString("userId");
				String loginService = jsonRequest.getString("service");
				String topicsRaw = jsonRequest.getString("topics");
				
				User user = userController.getByUserId(userId, loginService);
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
		
		get("/user/get/:username", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String username = request.params(":username");

				User user = userController.get(username, null);
				jsonResponseMessage.put("user", gson.toJson(user));

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
				String userId = jsonRequest.getString("userId");
				String token = jsonRequest.getString("token");
				String service = jsonRequest.getString("service");
				String client = jsonRequest.getString("client");

				logger.info("check user: " + name + " userId: " + userId + " token: " + token + " service " + service + " client " + client);
				
				if(tokenValidatorFactory.get(service, token, userId).validate()) {
					User user = userController.getByUserId(userId, service);
					if(user != null) {
						jsonResponseMessage.put("message", "ok");
					}else {
						user = userController.add(new User(userId, service, name));
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
