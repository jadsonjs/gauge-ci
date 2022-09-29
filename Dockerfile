
# docker build . -t jadsonjs/gauge-ci:v1.0.0

# build #
FROM gradle:7.5-jdk11 as builder
WORKDIR /app
COPY . .
RUN ./gradlew clean build

# docker push jadsonjs/gauge-ci:v1.0.0

# execution #
FROM adoptopenjdk:11-jdk-openj9
COPY --from=builder /app/build/libs/gauge-ci*.jar gauge-ci.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/gauge-ci.jar"]