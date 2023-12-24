# Spring Boot CI CD Playground

This is a proof of concept (POC) application. The goal is to create a CI/CD pipeline for a Spring Boot application. This can serve as a foundation to streamline CI/CD for any Java Spring Boot based application.

## Technologies
- Language: Java
- Back-end Framework: Spring Boot
- Testing Framework: JUnit 5
- Build Tool: Maven
- Database: Postgres
- Deployment: AWS Elastic Beanstalk
- CI-CD Tool: Github Actions

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/sakibul-munna/Spring-Boot-CI-CD-Playground.git
   ```
2. **Navigating to the Back-end folder**
   Remember to navigate to the back-end folder before running any maven or spring boot related commands.
   ```bash
   cd back-end
   ```
3. ** Run Only The Unit Tests **
   ```bash
   cd back-end
   mvn test
   ```
4. ** Run Only The Integration Tests **
   ```bash
   cd back-end
   mvn verify
   ```
3. ** Build docker image and pushing it to Docker Hub **
   As I used JIB plugin in the pom.xml file, JIB will automatically build and push the docker image. I have also configured that ```mvn package``` or ```./mvnw package``` command will build and push the docker image through JIB.

## File and Folder Structure
- The main purpose of _docker-compose.yaml_ file is to run Postgres db. But it also has sufficient commands to pull the latest image of this application and run it.
- The _.github/workflows_ directory contains all the workflows related to CI/CD pipeline. 
- The _back-end_ folder contains main spring boot application. The structure inside this _back-end_ folder is like a typical Spring Boot application.

## CI_CD Notes
To get an overview about the basics of about the CI-CD pipeline, have a read on this notion site, [CI/CD Notes](https://sakibul-munna.notion.site/CI-CD-Notes-1e0a6ce10b1a4a0d9622d48cb796dc2a)
