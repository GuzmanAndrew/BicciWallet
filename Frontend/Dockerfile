FROM node:18 AS build

WORKDIR /app

# Instalar dependencias
COPY package.json package-lock.json ./
RUN npm install

# Copiar el código fuente
COPY . .

# Crear el servicio StorageService que maneja localStorage de forma segura
RUN mkdir -p src/app/services && \
   echo "import { Injectable, Inject, PLATFORM_ID } from '@angular/core'; \n\
   import { isPlatformBrowser } from '@angular/common'; \n\
   \n\
   @Injectable({ \n\
   providedIn: 'root' \n\
   }) \n\
   export class StorageService { \n\
   private isBrowser: boolean; \n\
   \n\
   constructor(@Inject(PLATFORM_ID) platformId: Object) { \n\
   this.isBrowser = isPlatformBrowser(platformId); \n\
   } \n\
   \n\
   getItem(key: string): string | null { \n\
   if (this.isBrowser) { \n\
   return localStorage.getItem(key); \n\
   } \n\
   return null; \n\
   } \n\
   \n\
   setItem(key: string, value: string): void { \n\
   if (this.isBrowser) { \n\
   localStorage.setItem(key, value); \n\
   } \n\
   } \n\
   \n\
   removeItem(key: string): void { \n\
   if (this.isBrowser) { \n\
   localStorage.removeItem(key); \n\
   } \n\
   } \n\
   }" > src/app/services/storage.service.ts

# Compilar en modo desarrollo para evitar errores de presupuesto
# Y con SSR deshabilitado para evitar errores de localStorage
RUN node --max_old_space_size=4096 ./node_modules/.bin/ng build --configuration development

FROM nginx:alpine
COPY --from=build /app/dist/web-wallet/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponer puerto 4200
EXPOSE 4200