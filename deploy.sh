#!/bin/bash

BASE_DIR=$(pwd)
echo "🚀 Iniciando despliegue desde $BASE_DIR"

echo "🔍 Verificando requisitos..."
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado o no está en el PATH"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose no está instalado o no está en el PATH"
    exit 1
fi

echo "🐳 Iniciando los contenedores Docker..."
cd "$BASE_DIR/Backend"

for service in ms-users ms-accounts ms-transactions; do
    if [ ! -f "$service/Dockerfile" ]; then
        echo "❌ No se encontró el Dockerfile para $service"
        exit 1
    fi
done

docker-compose up -d --build

echo "✅ Verificando el estado de los contenedores..."
sleep 10

if docker ps | grep -q "ms-users"; then
    echo "✅ Microservicio de Usuarios iniciado correctamente"
else
    echo "❌ Error al iniciar el microservicio de Usuarios"
fi

if docker ps | grep -q "ms-accounts"; then
    echo "✅ Microservicio de Cuentas iniciado correctamente"
else
    echo "❌ Error al iniciar el microservicio de Cuentas"
fi

if docker ps | grep -q "ms-transactions"; then
    echo "✅ Microservicio de Transacciones iniciado correctamente"
else
    echo "❌ Error al iniciar el microservicio de Transacciones"
fi

if docker ps | grep -q "web-wallet-frontend"; then
    echo "✅ Frontend iniciado correctamente"
else
    echo "❌ Error al iniciar el Frontend"
fi

echo "🌐 BicciWallet desplegada con éxito!"
echo "📱 Frontend disponible en: http://localhost"
echo "🔌 APIs disponibles en:"
echo "   - Users: http://localhost:8081"
echo "   - Accounts: http://localhost:8082" 
echo "   - Transactions: http://localhost:8083"
echo ""
echo "💻 Para ver logs: docker-compose logs -f"
echo "💻 Para detener la aplicación: cd Backend && docker-compose down"
