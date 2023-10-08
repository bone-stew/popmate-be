FROM openjdk:17-jdk
LABEL maintainer="dyffh1031@gmail.com"
ARG JAR_FILE=build/libs/popmate-0.0.1-SNAPSHOT.jar
ENV JASYPT_PW default
ADD ${JAR_FILE} docker-springboot.jar
CMD ["java","-Dspring.profiles.active=prod","-Djasypt.encryptor.password=${JASYPT_PW}","-jar","/docker-springboot.jar"]
