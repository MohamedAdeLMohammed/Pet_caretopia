FROM maven:3.8.4-openjdk-17-slim

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 8088

CMD ["java", "-jar", "target/*.jar"] 