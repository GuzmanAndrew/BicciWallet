#!/bin/bash

echo "🚀 Compilando microservicios..."

# Compilar ms-users
echo "📦 Compilando ms-users..."
cd Backend/ms-users
./mvnw clean package -DskipTests
cd ../..

# Compilar ms-accounts
echo "📦 Compilando ms-accounts..."
cd Backend/ms-accounts
./mvnw clean package -DskipTests
cd ../..

# Compilar ms-transactions
echo "📦 Compilando ms-transactions..."
cd Backend/ms-transactions
./mvnw clean package -DskipTests
cd ../..

# Iniciar docker-compose
echo "🐳 Iniciando los contenedores Docker..."
cd Backend
docker-compose up -d --build

echo "✅ Web Wallet desplegada en http://localhost"
