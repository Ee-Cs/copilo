FROM maven:3.9.9-eclipse-temurin-23 AS maven_tool
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install

FROM eclipse-temurin:23
COPY --from=maven_tool /tmp/target/Study17-jar-with-dependencies.jar application.jar
ENTRYPOINT ["java","-jar","application.jar"]