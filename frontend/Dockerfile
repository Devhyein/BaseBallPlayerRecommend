FROM node:lts-alpine

# install simple http server for serving static content
RUN npm install -g http-server

# make the 'app' folder the current working directory
WORKDIR /app

# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY . .

# install project dependencies
RUN yarn install

# build app for production with minification
RUN yarn run build

EXPOSE 8901
ENTRYPOINT [ "http-server", "-p", "8901", "/app/dist" ]