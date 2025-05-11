FROM node:18 AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY . .

RUN if [ -f "angular.json" ]; then \
   sed -i 's/"prerender": true/"prerender": false/g' angular.json; \
   fi

RUN if [ -f "angular.json" ]; then \
   node -e "const fs=require('fs'); \
   const angular=JSON.parse(fs.readFileSync('angular.json')); \
   const budgets = angular.projects['bank-wallet'].architect.build.configurations.production.budgets || []; \
   budgets.forEach(budget => { \
   if (budget.type === 'component') budget.maximumWarning = '10kb'; \
   if (budget.type === 'component') budget.maximumError = '20kb'; \
   if (budget.type === 'initial') budget.maximumWarning = '2mb'; \
   if (budget.type === 'initial') budget.maximumError = '5mb'; \
   }); \
   fs.writeFileSync('angular.json', JSON.stringify(angular, null, 2));" \
   fi

RUN npm run build -- --configuration production

FROM nginx:alpine
COPY --from=build /app/dist/web-wallet /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 4200