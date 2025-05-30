FROM maven:3.9.9-eclipse-temurin-24 AS maven_tool
COPY third/src /tmp/third/src
COPY pom.xml /tmp/
COPY first/pom.xml /tmp/first/pom.xml
COPY second/pom.xml /tmp/second/pom.xml
COPY third/pom.xml /tmp/third/pom.xml
WORKDIR /tmp/
RUN mvn --quiet --projects third clean package

FROM eclipse-temurin:24
COPY --from=maven_tool /tmp/third/target/Study26-third-1.0.0-SNAPSHOT.jar application.jar
ENTRYPOINT ["java","--enable-native-access=ALL-UNNAMED","-jar","application.jar"]