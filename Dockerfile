FROM eclipse-temurin:17-jdk-jammy AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
COPY --from=build target/papeleria-0.0.1-SNAPSHOT.jar papeleria.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","papeleria.jar"]