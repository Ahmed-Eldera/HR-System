FROM eclipse-temurin

WORKDIR /app

COPY build/libs/hr-0.0.1-SNAPSHOT.jar /app/hr.jar

EXPOSE 8080

LABEL org.opencontainers.image.source="https://github.com/Ahmed-Eldera/HR-System"


CMD ["java" , "-jar" , "hr.jar"]
