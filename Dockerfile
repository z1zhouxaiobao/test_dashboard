# 前端构建
FROM node:18-alpine AS frontend
WORKDIR /fe
COPY frontend/package.json frontend/package-lock.json* ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# 后端构建（把前端 dist 拷进 static）
FROM maven:3.9-eclipse-temurin-8 AS backend
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src
COPY --from=frontend /fe/dist ./src/main/resources/static
RUN mvn -B -DskipTests package \
 && cp target/liquicool-backend-*.jar /app/app.jar

# 运行
FROM eclipse-temurin:8-jre-jammy
WORKDIR /app
RUN mkdir -p /app/uploads
COPY --from=backend /app/app.jar /app/app.jar
COPY uploads/ /app/uploads/
ENV FILE_UPLOAD_PATH=/app/uploads
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms256m -Xmx512m -Duser.timezone=Asia/Shanghai"
EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
