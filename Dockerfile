FROM tomcat:latest

COPY ./build/libs/dbPerformanceTest-0.0.1-SNAPSHOT-plain.jar /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]
