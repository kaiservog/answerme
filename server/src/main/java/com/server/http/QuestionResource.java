package com.server.http;

import static spark.Spark.post;
import static spark.Spark.get;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.server.conf.Configuration;
import com.server.controller.QuestionController;
import com.server.controller.TagController;
import com.server.controller.UserController;
import com.server.model.Question;
import com.server.model.Tag;
import com.server.model.User;

import redis.clients.jedis.Jedis;

public class QuestionResource {

	private static final Logger logger = LoggerFactory.getLogger(QuestionResource.class);

	@Inject
	private UserController userController;
	
	@Inject
	private TagController tagController;
	
	@Inject
	private QuestionController questionController;
	
	@Inject
	private Configuration configuration;
	private Jedis jedis;
	
	private static final Gson gson = new Gson();
	
	public void registerResource() {
		jedis = new Jedis(configuration.getCacheAddress(), configuration.getCachePort());
		
		post("/question/add", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				String queristString = jsonRequest.getString("querist");
				String questionString = jsonRequest.getString("question");
				String tagString = jsonRequest.getString("tag");
				
				Tag tag = tagController.findByName(tagString);
				if (tag == null) {
					tag = new Tag(tagString);
					tagController.add(tag);
				}
				User querist = userController.get(queristString);
				
				Question question = new Question(querist, null, tag, questionString, null);
				questionController.add(question);
				
				jedis.lpush(tag.getName(), String.valueOf(question.getId()));
				
				jsonResponseMessage.put("message", "ok");

			} catch (Exception e) {
				logger.error("Error in request /add", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
		
		get("/question/find/:responder/:tag", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String responderString = request.params(":responder");
				String tagString = request.params(":tag");
				
				String questionId = jedis.rpop(tagString);
				if (questionId != null) {
					Question question = questionController.find(Integer.valueOf(questionId));
					question.setResponder(userController.get(responderString));
					questionController.update(question);
					
					jsonResponseMessage.put("message", "ok");
					jsonResponseMessage.put("question", gson.toJson(question));
				} else {
					jsonResponseMessage.put("message", "notFound");					
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
