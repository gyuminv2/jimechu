CURRENT_DIR = $(shell pwd)

jar: secret-key
	@echo "$(CYAN)Building jar file...$(RESET)"
	@cd ${CURRENT_DIR}/backend/jimechu && sh gradlew clean build -x test # test 무시하고 jar 파일 생성
	@echo

.PHONY: jar