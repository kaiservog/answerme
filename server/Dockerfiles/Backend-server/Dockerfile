FROM centos

RUN yum -y install java-1.8.0-openjdk-devel.x86_64

ADD target/server-0.0.1-SNAPSHOT.jar /

CMD ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]

EXPOSE 4567
