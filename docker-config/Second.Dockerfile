FROM maven:3.9.9-eclipse-temurin-24 AS maven_tool
COPY second/src /tmp/second/src
COPY pom.xml /tmp/
COPY first/pom.xml /tmp/first/pom.xml
COPY second/pom.xml /tmp/second/pom.xml
COPY third/pom.xml /tmp/third/pom.xml
WORKDIR /tmp/
RUN mvn --quiet --projects second clean package

FROM eclipse-temurin:24
COPY --from=maven_tool /tmp/second/target/Study26-second-1.0.0-SNAPSHOT.jar application.jar
ENTRYPOINT ["java","--enable-native-access=ALL-UNNAMED","-jar","application.jar"]