FROM openjdk:17
EXPOSE 9092
ADD target/daily-expenses.jar daily-expenses.jar
ENTRYPOINT ["java","-jar","daily-expenses.jar"]