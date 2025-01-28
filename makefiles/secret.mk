CURRENT_DIR = $(shell pwd)

YML_PATH = ${CURRENT_DIR}/backend/jimechu/src/main/resources/application.yml

secret-key:
	@echo "$(RED)Generating new JWT secret key...$(RESET)"
	@NEW_KEY=$$(openssl rand -base64 32); \
	sed -i.bak "s|^  secret-key:.*|  secret-key: $${NEW_KEY}|" $(YML_PATH); \
	echo "\033[1mNew secret-key -> $(BLUE)$${NEW_KEY}$(RESET)"; \
	rm -f $(YML_PATH).bak
	@echo

.PHONY: secret-key