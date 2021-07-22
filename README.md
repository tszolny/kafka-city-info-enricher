# Project Name
> Kafka City Info Enricher

## General Information
Program:
1. Reads a message from “input_topic” in Kafka.
2. Enriches the data payload with Country taken from a database
3. Enriches the data payload with Population taken from a REST/HTTP endpoint
4. Sends the enriched message to “output_topic” in Kafka


## Setup
To run the application, you need to have Java 8.

## Usage
In order to run the application, run kafka and wiremock standalone servers before. 

Then open console and go to the project's root directory called "city-info-enricher" and execute the following command:

`gradlew run`

If you want to test the application, you don't have to run standalone kafka and wiremock servers.
The application uses embedded servers. 

Just open console and go to the project's root directory called "city-info-enricher". 

Then type below command:

`gradlew clean test`

There is a well-known issue when you run kafka on Windows. It prevents kafka from running properly.

The problem has been described here: https://issues.apache.org/jira/browse/KAFKA-8145

Therefore the best way to test this application is to run the command on Linux.
However you can test it on Windows from Intellij IDEA, by running integration test not using gradle command.
