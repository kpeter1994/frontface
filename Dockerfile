FROM quay.io/drsylent/cubix/exam/base:java21-alpine


WORKDIR /app
COPY target/*.jar /app/app.jar


LABEL com.cubixedu.training.exam="KOVACS_PETER"
LABEL com.cubixedu.training.application="devops-exam-frontface"

ENV MANAGEMENT_SERVER_PORT=8081
ENV FRONTFACE_TOKEN=${FRONTFACE_TOKEN}

# Alkalmazás indítása
CMD ["java", "-jar", "/app/app.jar"]
