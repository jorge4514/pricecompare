# Usa una imagen base de Tomcat para ejecutar WARs
FROM tomcat:9.0-jdk11-openjdk-slim

# Copiar el archivo WAR al contenedor de Tomcat
COPY target/supermarketapi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto 8080
EXPOSE 8080

# Comando para iniciar Tomcat
CMD ["catalina.sh", "run"]
