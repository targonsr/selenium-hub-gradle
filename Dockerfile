FROM ubuntu:latest

ENV USER="tests" \
    TZ="Europe/Warsaw" \
    AUTOMATION_DIR="/opt/automation" \
    JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"

VOLUME [ \
    "report:${AUTOMATION_DIR}/report", \
    "build:${AUTOMATION_DIR}/build" \
]

RUN apt-get update -qq \
    && apt-get install -qq -y --no-install-recommends \
        nano \
        openjdk-8-jdk \
        firefox \
        patch \
        gradle \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN useradd -ms /bin/bash $USER \
    && mkdir ${AUTOMATION_DIR} \
    && install -o $USER -g $USER -d /home/$USER/.gradle/caches /home/$USER/.gradle/daemon \
    && ln -snf /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} >/etc/timezone

WORKDIR ${AUTOMATION_DIR}

COPY . ${AUTOMATION_DIR}

USER $USER
