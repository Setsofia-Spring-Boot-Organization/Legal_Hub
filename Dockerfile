#build the app
FROM maven:3.8.1-openjdk-17 AS BUILD
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

#create the final image
FROM openjdk:17.0.1-slim
WORKDIR /app
COPY --from=BUILD /app/target/legal_hub-0.0.1-SNAPSHOT.jar legal_hub.jar

#set the port
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "legal_hub.jar"]
