#!/bin/bash

./mvnw clean package
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 1 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 2 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 5 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar in-memory 10 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 1 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 2 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 5 10000
java -jar target/hw1-client-0.0.1-SNAPSHOT.jar file-based 10 10000