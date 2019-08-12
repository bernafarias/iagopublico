FROM tomcat:8.0.20-jre8
COPY iago.war /usr/local/tomcat/webapps/iago.war
EXPOSE 8080