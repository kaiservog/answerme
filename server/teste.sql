select * from user, topic, user_topic where user.id_user = user_topic.id_user and topic.id_topic = user_topic.id_topic;
select * from user;
select * from question;

delete from question;
delete from user_topic;
delete from topic;
delete from user;


drop table user_topic;
drop table topic;
drop table user;
