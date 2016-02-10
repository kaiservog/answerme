package com.server.http;

import static spark.Spark.get;
import static spark.Spark.post;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.server.conf.RedisClient;
import com.server.controller.QuestionController;
import com.server.controller.TopicController;
import com.server.controller.UserController;
import com.server.model.Question;
import com.server.model.QuestionView;
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
	private RedisClient redisClient;

	private final Gson gson = new Gson();

	public void registerResource() {
		post("/question/add", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				String queristString = jsonRequest.getString("userId");
				String loginService = jsonRequest.getString("loginService");
				String questionString = jsonRequest.getString("question");
				String topicString = jsonRequest.getString("topic");

				Topic topic = topicController.findByName(topicString);
				if (topic == null) {
					topic = new Topic(topicString);
					topicController.add(topic);
				}
				User querist = userController.get(queristString, loginService);

				Question question = new Question(querist, null, topic, questionString, null, 0);
				questionController.add(question);

				redisClient.getJedis().lpush(topic.getName(), String.valueOf(question.getId()));

				jsonResponseMessage.put("message", "ok");

			} catch (Exception e) {
				logger.error("Error in request /find", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});

		get("/question/accept/:token/:loginService/:questionId", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String tokenString = request.params(":token");
				String loginService = request.params(":loginService");
				String questionId = request.params(":questionId");

				if (questionId != null) {
					Question question = questionController.find(Integer.valueOf(questionId));
					question.setResponder(userController.get(tokenString, loginService));
					question.setTtl(System.currentTimeMillis());
					questionController.update(question);

					jsonResponseMessage.put("message", "ok");
				} else {
					jsonResponseMessage.put("message", "invalid questionId");
				}

			} catch (Exception e) {
				logger.error("Error in request /find", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});

		get("/question/reject/:questionId", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String questionId = request.params(":questionId");

				if (questionId != null) {
					Question question = questionController.find(Integer.valueOf(questionId));
					question.setTtl(0);
					questionController.update(question);

					redisClient.getJedis().lpush(question.getTopic().getName(), String.valueOf(question.getId()));

					jsonResponseMessage.put("message", "ok");
				} else {
					jsonResponseMessage.put("message", "invalid question id");
				}

			} catch (Exception e) {
				logger.error("Error in request /find", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});

		get("/question/find/:topic", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String topicString = request.params(":topic");

				String questionId = redisClient.getJedis().rpop(topicString);
				if (questionId != null) {
					Question question = questionController.find(Integer.valueOf(questionId));
					question.setTtl(System.currentTimeMillis());
					questionController.update(question);

					jsonResponseMessage.put("message", "ok");
					jsonResponseMessage.put("question", new JSONObject(gson.toJson(new QuestionView(question))));
				} else {
					jsonResponseMessage.put("message", "notFound");
				}

				jsonResponseMessage.put("next", 2000L);

			} catch (Exception e) {
				logger.error("Error in request /find", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});

		get("/question/answer/:token/:loginService/:questionId/:answer", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				String tokenString = request.params(":token");
				String loginService = request.params(":loginService");
				String questionId = request.params(":questionId");
				String answer = request.params(":answer");

				if (questionId != null) {
					Question question = questionController.find(Integer.valueOf(questionId));
					// TODO: MELHORAR COMPARACAO USER
					if (question.getResponder() != null && question.getResponder().getUserId().equals(tokenString) &&  
							question.getResponder().getLoginService().equals(loginService)) {
						question.setTtl(0);
						question.setAnswer(answer);
						questionController.update(question);

						jsonResponseMessage.put("message", "ok");
					} else {
						jsonResponseMessage.put("message", "question expired");
					}
				} else {
					jsonResponseMessage.put("message", "invalid questionId");
				}

			} catch (Exception e) {
				logger.error("Error in request /find", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
	}
}
