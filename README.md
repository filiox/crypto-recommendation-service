# Crypto Recommendation Service

This is the crypto recommendation service developed as part of the requirements provided by XM for the technical task.

## Requirements

- Java 21
- Docker
- Kubectl
- Gradle

## Build

./gradlew clean build

## API Documentation

API Documentation can be found under the `documentation` folder in the project. It was generated using the [Redocly](https://redocly.com/) tool.

## Deployment

For deploying this to Kubernetes, you can execute the `deploy.sh` script that can be found under the `deployment` folder. The script will build and push the Docker image and then deploy it to Kubernetes.

Before running this script, make sure you are logged in to Docker, and you have installed the pre-requisites.

deploy.sh <username>

- **Exposed endpoint:** [http://localhost:30000](http://localhost:30000)

## Additional Links
* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
* [Docker Documentation](https://docs.docker.com/)
* [Kubectl Documentation](https://kubernetes.io/docs/reference/kubectl/)
* [Redocly Documentation](https://redocly.com/docs/)