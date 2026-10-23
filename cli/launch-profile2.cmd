# launch app with profile connecting to Neon db.
# http://localhost:8085/login
#
mvn clean package -DskipTests -Pproduction2
java "-Dspring.profiles.active=production2" -jar target\colpoapp-vaadin-1.4-SNAPSHOT.jar -d64 -Xmx8g -XX:+CrashOnOutOfMemoryError