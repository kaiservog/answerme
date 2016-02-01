package com.server.http;

import static spark.Spark.get;
import static spark.Spark.post;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.server.conf.Configuration;
import com.server.controller.QuestionController;
import com.server.controller.TopicController;
import com.server.controller.UserController;
import com.server.model.Question;
import com.server.model.Topic;
import com.server.model.User;

public class QuestionResource {

	private static final Logger logger = LoggerFactory.getLogger(QuestionResource.class);

	@Inject
	private UserController userController;
	
	@Inject
	private TopicController topicController;
	
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
				String topicString = jsonRequest.getString("topic");
				
				Topic topic = topicController.findByName(topicString);
				if (topic == null) {
					topic = new Topic(topicString);
					topicController.add(topic);
				}
				User querist = userController.get(queristString);
				
				Question question = new Question(querist, null, topic, questionString, null);
				questionController.add(question);
				
				jedis.lpush(topic.getName(), String.valueOf(question.getId()));
				
				jsonResponseMessage.put("message", "ok");

			} catch (Exception e) {
				logger.error("Error in request /add", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
		
		get("/question/find/:responder/:topic", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String responderString = request.params(":responder");
				String topicString = request.params(":topic");
				
				String questionId = jedis.rpop(topicString);
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
