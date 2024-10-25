#!/bin/bash

# Check if Docker username is provided
if [ -z "$1" ]; then
    echo "Usage: ./deploy.sh <docker_username>"
    exit 1
fi

# Set variables
DOCKER_USER="$1"
IMAGE_NAME="recommendation-service"
TAG="latest"

# Step 1: Build the Docker image
echo "Building Docker image..."
docker build -t "$DOCKER_USER/$IMAGE_NAME:$TAG" .

# Step 2: Push the Docker image to Docker Hub
echo "Pushing Docker image to Docker Hub..."
docker push "$DOCKER_USER/$IMAGE_NAME:$TAG"

# Step 3: Apply the Kubernetes deployment
echo "Applying Kubernetes deployment..."
kubectl apply -f deployment.yaml -n

# Step 4: Apply the Kubernetes service
echo "Applying Kubernetes service..."
kubectl apply -f service.yaml -n

echo "Deployment complete."