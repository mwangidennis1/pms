FROM openjdk:17-jdk

#workdir
WORKDIR /myapp

COPY . .

RUN ./mvnw package

EXPOSE 8080

CMD ["java","-jar","./target/maya-0.0.1-SNAPSHOT.jar"]