# Colposcopy Vaadin Application



## production build
Production build will package clientside resources.

> mvn clean package -Pproduction

## build image

> docker build -f DockerfileApp -t colpoapp-vaadin .

notes for dockerfile:

please update dockerfile to include -Dspring.profiles.active=prod so that production properties got properly applied.
```bash
ENTRYPOINT java -jar -Dspring.profiles.active=prod /myapp/colpoapp-vaadin-1.0-SNAPSHOT.jar -d64 -Xmx8g -XX:+CrashOnOutOfMemoryError
```
please make sure that application image used in docker build is the same as in docker compose.


```
# build and run app
mvn clean package -Pproduction -DskipTests 
docker build -f DockerfileApp -t colpoapp-vaadin-4 .
#run via docker. issue will be hostname postgresql not found. to make this work, you need to run via docker-compose.
docker run -p 8080:8080 colpoapp-vaadin-4:latest
# run via docker-compose
docker-compose up -d
```
notes:

- about hostnames in docker environment. 

When you run docker-compose up, Docker Compose automatically creates a network for your app and adds all the services in your app to that network. Containers on the same network can reach each other using the service name as the hostname.

issues:

- when running with -Dspring.profiles.active=prod application-prod.properties is not being overlapped!.

