# Frontend/Dockerfile
FROM node:18 AS build

WORKDIR /app

# Instalar dependencias
COPY package.json package-lock.json ./
RUN npm install

# Copiar el código fuente
COPY . .

# Crear un script sencillo para modificar angular.json
RUN echo '#!/usr/bin/env node \n\
   const fs = require("fs"); \n\
   try { \n\
   const angularConfigPath = "./angular.json"; \n\
   if (fs.existsSync(angularConfigPath)) { \n\
   const angular = JSON.parse(fs.readFileSync(angularConfigPath, "utf8")); \n\
   const projectName = Object.keys(angular.projects)[0]; \n\
   const buildOptions = angular.projects[projectName].architect.build; \n\
   \n\
   // Desactivar prerendering si existe \n\
   if (buildOptions.configurations && buildOptions.configurations.production) { \n\
   if (buildOptions.configurations.production.prerender) { \n\
   buildOptions.configurations.production.prerender = false; \n\
   } \n\
   } \n\
   \n\
   // Aumentar presupuestos \n\
   if (buildOptions.configurations && buildOptions.configurations.production && buildOptions.configurations.production.budgets) { \n\
   const budgets = buildOptions.configurations.production.budgets; \n\
   budgets.forEach(budget => { \n\
   if (budget.type === "component") { \n\
   budget.maximumWarning = "10kb"; \n\
   budget.maximumError = "20kb"; \n\
   } \n\
   if (budget.type === "initial") { \n\
   budget.maximumWarning = "2mb"; \n\
   budget.maximumError = "5mb"; \n\
   } \n\
   }); \n\
   } \n\
   \n\
   fs.writeFileSync(angularConfigPath, JSON.stringify(angular, null, 2)); \n\
   console.log("Angular config updated successfully!"); \n\
   } else { \n\
   console.log("angular.json not found"); \n\
   } \n\
   } catch (error) { \n\
   console.error("Error updating angular.json:", error); \n\
   process.exit(1); \n\
   }' > update-angular-config.js && chmod +x update-angular-config.js && node update-angular-config.js

# Construir sin utilizar --prod que está obsoleto
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist/web-wallet /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto 4200 para cumplir con CORS
EXPOSE 4200