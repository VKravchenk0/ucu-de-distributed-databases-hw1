# ucu-de-distributed-databases-hw1

cd client
mvn clean package
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar inmemory 10 1000

cd server
mvn spring-boot:run