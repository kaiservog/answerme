select * from user, topic, user_topic where user.id_user = user_topic.id_user and topic.id_topic = user_topic.id_topic;
select * from user;