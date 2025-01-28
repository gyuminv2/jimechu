DOCKER_COMPOSE_PATH = ./docker-compose.yml
DOCKER_COMPOSE = docker compose --file $(DOCKER_COMPOSE_PATH)

up: jar
	@echo "$(MAGENTA)Starting up containers...$(RESET)"
	$(DOCKER_COMPOSE) up --build -d # 컨테이너 생성
	@echo

down:
	@echo "$(YELLOW)Stopping containers...$(RESET)"
	$(DOCKER_COMPOSE) down --volumes	# Compose 정의 파일의 데이터 볼륨 삭제

clean:
	@echo "$(WHITE)Cleaning up containers...$(RESET)"
	$(DOCKER_COMPOSE) down --rmi all --volumes	# 모든 이미지 삭제
	@echo

fclean: clean
	@echo "$(WHITE)Removing unused resources...$(RESET)"
	docker system prune -af	# 사용하지 않는 컨테이너 리소스 모두 삭제
	@echo

re:
	@echo "$(WHITE)Restarting containers...$(RESET)"
	make fclean
	make all

.PHONY: up down clean fclean re