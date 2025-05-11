FROM node:18 AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

RUN mkdir -p /app/src/polyfills && \
   echo "/** localStorage polyfill for build */\n\
   if (typeof window === 'undefined') {\n\
   global.window = {};\n\
   }\n\
   if (typeof localStorage === 'undefined') {\n\
   global.localStorage = {\n\
   getItem: () => null,\n\
   setItem: () => null,\n\
   removeItem: () => null,\n\
   clear: () => null\n\
   };\n\
   }" > /app/src/polyfills/local-storage.js

ENV NODE_OPTIONS=--require=/app/src/polyfills/local-storage.js

RUN npm run build --prod

FROM nginx:alpine
COPY --from=build /app/dist/web-wallet /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 4200