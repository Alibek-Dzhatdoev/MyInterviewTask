FROM maven AS maven
WORKDIR /app
COPY ./pom.xml ./
COPY ./src ./src
RUN mvn clean package

FROM openjdk:17
COPY --from=maven ./app/target/MyInterviewTask-0.0.1-SNAPSHOT.jar ./app.jar
ENTRYPOINT java ${JAVA_OPTS} -jar app.jar
