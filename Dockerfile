FROM openjdk:8-alpine

COPY target/uberjar/luminapp.jar /luminapp/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/luminapp/app.jar"]
