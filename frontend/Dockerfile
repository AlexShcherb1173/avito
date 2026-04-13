FROM node:20-bullseye AS build

WORKDIR /build

COPY package*.json ./
RUN npm ci

COPY . .

ARG REACT_APP_API_URL=/api
ENV REACT_APP_API_URL=${REACT_APP_API_URL}

RUN npm run build

FROM nginx:1.27-alpine

RUN rm -f /etc/nginx/conf.d/default.conf

COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/conf.d/ /etc/nginx/conf.d/
COPY --from=build /build/dist /opt/web/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]