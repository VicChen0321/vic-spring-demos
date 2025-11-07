APP_NAME = spring-boot-websocket-stomp
IMAGE_NAME = $(APP_NAME):latest
CONTAINER_NAME = $(APP_NAME)-container
PORT = 8080

.PHONY: build run stop clean logs restart

build:
	docker build -t $(IMAGE_NAME) .

run:
	docker run -d --name $(CONTAINER_NAME) -p $(PORT):8080 $(IMAGE_NAME)

stop:
	docker stop $(CONTAINER_NAME) || true
	docker rm $(CONTAINER_NAME) || true

clean: stop
	docker rmi $(IMAGE_NAME) || true

logs:
	docker logs -f $(CONTAINER_NAME)

restart: stop run

start: build run

status:
	docker ps | grep $(CONTAINER_NAME) || echo "Container not running"

help:
	@echo "Available commands:"
	@echo "  build    - Build Docker image"
	@echo "  run      - Run container"
	@echo "  stop     - Stop and remove container"
	@echo "  clean    - Stop container and remove image"
	@echo "  logs     - Show container logs"
	@echo "  restart  - Stop and start container"
	@echo "  start    - Build and run (full startup)"
	@echo "  status   - Check container status"
