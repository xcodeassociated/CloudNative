FROM node:latest
ENV APP_HOME=/usr/local/app/
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME
COPY package.json $APP_HOME
COPY package-lock.json $APP_HOME
RUN npm install
COPY . $APP_HOME
EXPOSE 8090
EXPOSE 3000
CMD ["npm","start"]
