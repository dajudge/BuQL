FROM azul/zulu-openjdk:11.0.4

ENV DOCKER_URL=https://download.docker.com/linux/static/stable/x86_64
ENV DOCKER_VERSION=18.03.1-ce

RUN apt-get update && \
    apt-get install -y sudo bash curl && \
    rm -rf /var/lib/apt/lists/* && \
    curl -fsSL $DOCKER_URL/docker-$DOCKER_VERSION.tgz | tar zxvf - --strip 1 -C /usr/bin docker/docker