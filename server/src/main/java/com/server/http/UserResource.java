package com.server.http;

import static spark.Spark.get;
import static spark.Spark.post;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.server.controller.UserController;
import com.server.model.User;

public class UserResource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

	private static final Gson gson = new Gson();

	public static void registerResource(UserController userController) {
		post("/user/add", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				String firstName = jsonRequest.getString("firstName");
				String lastName = jsonRequest.getString("lastName");
				String phone = jsonRequest.getString("phone");
				String username = jsonRequest.getString("username");
				String password = jsonRequest.getString("password");

				userController.add(new User(firstName, lastName, phone, username, password));
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

				User user = userController.get(username);
				jsonResponseMessage.put("user", gson.toJson(user));

			} catch (Exception e) {
				logger.error("Error in request /get", e);
				jsonResponseMessage.put("user", "null");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
	}
}
