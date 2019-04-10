# Start with a base image containing Java runtime

################# PARENT/ BASE IMAGE ->

#FROM openjdk:8-jdk-alpine
#LABEL maintainer="krnbr@live.in"
#RUN mkdir -p /oauth/jks
#COPY neuw.jks /oauth/jks/neuw.jks
#COPY public.txt /oauth/jks/public.txt

#FROM registry.gitlab.com/neuw/boot:latest

FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="krnbr@live.in"

ENV SPRING_PROFILES_ACTIVE=dock
ENV NEUW_ENV=dock
ENV DB_PASSWORD=root

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG WAR_FILE=target/boot-*.war

# Add the application's jar to the container
ADD ${WAR_FILE} boot.war

#COPY neuw.jks /oauth/jks/neuw.jks
#COPY public.txt /oauth/jks/public.txt

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/boot.war"]