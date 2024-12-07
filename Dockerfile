# Java 17 기반 이미지 사용
FROM eclipse-temurin:17-jdk-alpine

# 애플리케이션 디렉터리 생성
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]