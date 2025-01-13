DOCKER_COMPOSE_FILE = ./docker-compose.yml
DOCKER_COMPOSE = docker compose --file $(DOCKER_COMPOSE_FILE)

all: jar up

jar:
	cd /Users/gyumpark/jimechu/backend/jimechu && sh gradlew clean build # jar 파일 생성

up:
	$(DOCKER_COMPOSE) up --build -d # 컨테이너 생성

down:
	$(DOCKER_COMPOSE) down --volumes	# Compose 정의 파일의 데이터 볼륨 삭제

clean:
	$(DOCKER_COMPOSE) down --rmi all --volumes	# 모든 이미지 삭제

fclean: clean
	docker system prune -af	# 사용하지 않는 컨테이너 리소스 모두 삭제

re:
	make fclean
	make all

.PHONY: all up down clean fclean re