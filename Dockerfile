FROM openjdk:11-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY . .
RUN ./gradlew build --exclude-task test

FROM openjdk:11-jre
WORKDIR /app
COPY --from=build /app/build/libs/case-backend-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "case-backend-0.0.1-SNAPSHOT.jar"]

