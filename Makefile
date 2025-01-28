include makefiles/colors.mk
include makefiles/docker.mk
include makefiles/secret.mk
include makefiles/build.mk

all: secret-key jar up

.PHONY: all