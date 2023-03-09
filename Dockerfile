FROM maven:3-jdk-8-alpine

#WORKDIR /usr/src/app

#COPY . /usr/src/app
#RUN mvn package

ENV PORT  8200
EXPOSE $PORT
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} helloworld.jar

#4개의 layer 로 구성해서 , 재 빌드시 cache 를 사용하여, build 속도를 향상 한다.
#FROM maven:3-jdk-8-alpine
#WORKDIR helloworld
#COPY --from=builder helloworld/dependencies/ ./
#COPY --from=builder helloworld/spring-boot-loader/ ./
#COPY --from=builder helloworld/snapshot-dependencies/ ./
#COPY --from=builder helloworld/application/ ./
#ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
ENTRYPOINT ["java","-jar","/helloworld.jar"]

#CMD [ "sh", "-c", "mvn -Dserver.port=${PORT} spring-boot:run" ]