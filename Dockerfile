FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# 1. Instalar herramientas básicas y dependencias
RUN apt-get update && apt-get install -y \
    curl git wget unzip nano vim net-tools openssh-server \
    openjdk-17-jdk \
    python3 python3-pip \
    iputils-ping gnupg2 ca-certificates \
    gnupg lsb-release software-properties-common \
    maven

# 2. Eliminar Node.js antiguo, instalar Node.js 20 y Angular CLI 17
RUN apt-get remove -y nodejs npm libnode* && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g @angular/cli@17

# 3. Crear usuario redteam y habilitar SSH
RUN useradd -m redteam && echo "redteam:redteam" | chpasswd && \
    mkdir /var/run/sshd && \
    sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

# 4. Clonar el repositorio BicciWallet
WORKDIR /home/redteam
RUN git clone https://github.com/GuzmanAndrew/BicciWallet.git

# 5. Instalar dependencias del frontend Angular
WORKDIR /home/redteam/BicciWallet/Frontend
RUN npm install

# 6. Exponer puertos necesarios
EXPOSE 8081 8082 8083 4200 22

# 7. Ejecutar microservicios y frontend en segundo plano
CMD service ssh start && \
    cd /home/redteam/BicciWallet/Backend/ms-users && mvn spring-boot:run > /dev/null 2>&1 & \
    cd /home/redteam/BicciWallet/Backend/ms-accounts && mvn spring-boot:run > /dev/null 2>&1 & \
    cd /home/redteam/BicciWallet/Backend/ms-transactions && mvn spring-boot:run > /dev/null 2>&1 & \
    cd /home/redteam/BicciWallet/Frontend && ng serve --host 0.0.0.0 --port 4200 > /dev/null 2>&1 & \
    tail -f /dev/null
