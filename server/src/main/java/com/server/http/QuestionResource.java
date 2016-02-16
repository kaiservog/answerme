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
import com.server.model.AnswerView;
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
				
				User querist = getRequestedUser(jsonRequest);
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
		
		//TODO vai ter que quebrar este metodo
		post("/question/find", (request, response) -> {
			response.type("application/json");
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonResponseMessage = new JSONObject();
			
			jsonResponseMessage.put("next", 2000L);
			try {
				JSONObject obj = new JSONObject(request.body());
				JSONObject jsonRequest = obj.getJSONObject("request");
				User user = getRequestedUser(jsonRequest);
				
				Long answeredQuestionId = getUserAnsweredQuestion(user.getId());
				
				if(answeredQuestionId != null) {
					Question answeredQuestion = questionController.find(answeredQuestionId);
					jsonResponseMessage.put("message", "ok");
					jsonResponseMessage.put("type", "answer");
					jsonResponseMessage.put("answer", new JSONObject(gson.toJson(new AnswerView(answeredQuestion))));
					jsonResponse.put("response", jsonResponseMessage);
					return jsonResponse;
				}
				
				String questionId = null;
				String topicFounded = null;
				
				for(String topic : getUserTopics(user)) {
					questionId = getJedisClient().getJedis().rpop(topic);
					topicFounded = topic;
					if(questionId != null) break;
				}
				
				if (questionId != null) {
					Question question = questionController.find(Long.valueOf(questionId));
					User responder = getRequestedUser(jsonRequest);
					if(question.getQuerist().equals(responder)) {
						getJedisClient().getJedis().rpush(topicFounded, questionId);
						jsonResponseMessage.put("message", "notFound");
					}else {
						question.resetTtl();
						questionController.update(question);
						
						jsonResponseMessage.put("message", "ok");
						jsonResponseMessage.put("type", "question");
						jsonResponseMessage.put("question", new JSONObject(gson.toJson(new QuestionView(question))));
					}
				} else {
					jsonResponseMessage.put("message", "notFound");
				}
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
				Long questionId = jsonRequest.getLong("questionId");
				
				Question question = questionController.find(questionId);
				if (question.getResponder() == null) {
					question.accept(getRequestedUser(jsonRequest));
					questionController.update(question);

					jsonResponseMessage.put("message", "ok");	
				} else {
					jsonResponseMessage.put("message", "question expired");
				}

			} catch (Exception e) {
				logger.error("Error in request /question/accept", e);
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
				Long questionId = jsonRequest.getLong("questionId");
				
				Question question = questionController.find(questionId);
				if (question != null && question.getResponder() == null) {
					question.reject();
					questionController.update(question);
					getJedisClient().getJedis().lpush(question.getTopic().getName(), String.valueOf(question.getId()));
					
					jsonResponseMessage.put("message", "ok");
				} else {
					jsonResponseMessage.put("message", "question expired");
				}
				
				jsonResponseMessage.put("next", 2000L);
			} catch (Exception e) {
				logger.error("Error in request /question/reject", e);
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
				Long questionId = jsonRequest.getLong("questionId");
				String answer = jsonRequest.getString("answer");
				
				Question question = questionController.find(questionId);
				// TODO: MELHORAR COMPARACAO USER
				if (question.getResponder() != null && 
						Long.valueOf(getUserId(token, loginService)).equals(question.getResponder().getId())) {
					
					question.answer(answer);
					questionController.update(question);
					registerUserAnsweredQuestion(question.getQuerist().getId(), question.getId());

					jsonResponseMessage.put("message", "ok");
				} else {
					jsonResponseMessage.put("message", "question expired");
				}

			} catch (Exception e) {
				logger.error("Error in request /question/answer", e);
				jsonResponseMessage.put("message", "error");
			}
			jsonResponse.put("response", jsonResponseMessage);
			return jsonResponse;
		});
	}
}
