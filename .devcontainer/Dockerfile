ARG VARIANT="17"
FROM mcr.microsoft.com/vscode/devcontainers/java:0-${VARIANT}

# Default MAVEN options
ARG INSTALL_MAVEN="true"
ARG MAVEN_VERSION=""

# MAVEN
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then \
    su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; \
    fi

# Copy check script
COPY check.sh /check.sh
RUN chmod +x /check.sh


