FROM openjdk:17-jdk
LABEL maintainer="dyffh1031@gmail.com"
ARG JAR_FILE=build/libs/popmate-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-Dspring.profiles.active=local","-Djasypt.encryptor.password=vkqapdlxm12#","-jar","/docker-springboot.jar"]