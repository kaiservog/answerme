package com.server.http;

import static spark.Spark.post;

import javax.inject.Inject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.server.controller.QuestionController;
import com.server.controller.TopicController;
import com.server.controller.UserController;
import com.server.model.Question;
import com.server.model.QuestionView;
import com.server.model.Topic;
import com.server.model.User;

public class QuestionResource extends Resource {

	private static final Logger logger = LoggerFactory.getLogger(QuestionResource.class);

	@Inject
	private TopicController topicController;
	
	@Inject
	private QuestionController questionController;
	
	private final Gson gson = new Gson();
	
	public void registerResource() {
		post("/question/add", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				String questionString = jsonRequest.getString("question");
				String topicString = jsonRequest.getString("topic");
				
				logger.info("Question add " + questionString);
				Topic topic = topicController.findByName(topicString);
				if (topic == null) {
					topic = new Topic(topicString);
					topicController.add(topic);
				}
				
				User querist = getResquestedUser(jsonRequest);
				Question question = new Question(querist, null, topic, questionString, null, 0);
				questionController.add(question);
				
				getJedisClient().getJedis().lpush(topic.getName(), String.valueOf(question.getId()));
				
				jsonResponseMessage.put("message", "ok");

			} catch (Exception e) {
				logger.error("Error in request /find", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
		
		post("/question/find", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				String topicString = jsonRequest.getString("topic");
				String token = jsonRequest.getString("token");
				String loginService = jsonRequest.getString("loginService");

				String questionId = getJedisClient().getJedis().rpop(topicString);
				if (questionId != null) {
					Question question = questionController.find(Integer.valueOf(questionId));
					User responder = getResquestedUser(jsonRequest);
					if(question.getQuerist().equals(responder)) {
						jsonResponseMessage.put("message", "notFound");
						getJedisClient().getJedis().rpush(topicString, questionId);
					}else {
						question.setTtl(System.currentTimeMillis());
						questionController.update(question);
						
						jsonResponseMessage.put("message", "ok");
						jsonResponseMessage.put("question", new JSONObject(gson.toJson(new QuestionView(question))));
					}
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

		post("/question/accept", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				Integer questionId = jsonRequest.getInt("questionId");
				
				if (questionId != null) {
					Question question = questionController.find(questionId);
					if (question.getResponder() == null) {
						question.setResponder(getResquestedUser(jsonRequest));
						question.setTtl(System.currentTimeMillis());
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
		
		post("/question/reject", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				Integer questionId = jsonRequest.getInt("questionId");
				
				if (questionId != null) {
					Question question = questionController.find(questionId);
					if (question != null && question.getResponder() == null) {
						question.setTtl(0);
						questionController.update(question);
						getJedisClient().getJedis().lpush(question.getTopic().getName(), String.valueOf(question.getId()));
						
						jsonResponseMessage.put("message", "ok");
					} else {
						jsonResponseMessage.put("message", "question expired");
					}
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

		post("/question/answer", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				String token = jsonRequest.getString("token");
				String loginService = jsonRequest.getString("loginService");
				Integer questionId = jsonRequest.getInt("questionId");
				String answer = jsonRequest.getString("answer");
				
				if (questionId != null) {
					Question question = questionController.find(questionId);
					// TODO: MELHORAR COMPARACAO USER
					if (question.getResponder() != null && 
							Long.valueOf(getUserId(token, loginService)).equals(question.getResponder().getId())) {
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
