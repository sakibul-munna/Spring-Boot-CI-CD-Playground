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
2. **Navigating to the Back-end folder:**
   ```bash
   cd back-end
   ```
   N.B.: Navigate to the back-end folder before running maven or spring boot related commands.
3. **Run Only The Unit Tests:**
   ```bash
   cd back-end
   mvn test
   ```
4. **Run Only The Integration Tests:**
   ```bash
   cd back-end
   mvn verify
   ```
5. **Build docker image for the first time and push it to Docker Hub:**
   ```bash
   cd back-end
   mvn compile jib:build -Ddocker.image.tag=$(date '+%d.%m.%Y.%H.%M.%S')
   ```
   I am using the [JIB Maven Plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin#quickstart) to build the docker image and push it to Docker Hub. So the given commands will create the image with current timestamp and will push it to Docker Hub.
6. **Build docker image after first time and push it to Docker Hub:**
   ```bash
   cd back-end
   mvn package
   ```
   In the JIB plugin configuration in the pom.xml file I have already configured that the ```mvn package``` or ```./mvnw package``` command will build and push the docker image through JIB. **_Note_**: _```mvn package``` command will run the integration tests first and then it will build the app._


## File and Folder Structure
- The primary purpose of _docker-compose.yaml_ file is to run Postgres db. But it also has sufficient commands to pull the latest image of this application and run it.
- The _.github/workflows_ directory contains all the workflows related to CI/CD pipeline. 
- The _back-end_ folder contains main spring boot application. The structure inside this _back-end_ folder is like a typical Spring Boot application.

## CI_CD Notes
To get an overview about the basics of about the CI-CD pipeline, have a read on this notion site, [CI/CD Notes](https://sakibul-munna.notion.site/CI-CD-Notes-1e0a6ce10b1a4a0d9622d48cb796dc2a)
