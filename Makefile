# Variables
IMAGE_NAME=school
VERSION=latest
GRADLE=./gradlew

.PHONY: all build clean up down

all: build

export-docs:
	@echo "Exporting static OpenAPI specification..."
	$(GRADLE) generateOpenApiDocs -Dorg.gradle.configuration-cache=false

build: export-docs
	@echo "Building ARM64 Spring Boot 4 image..."
	$(GRADLE) bootBuildImage --imageName=$(IMAGE_NAME):$(VERSION)

up:
	docker compose up -d --wait

down:
	docker compose down --remove-orphans -v