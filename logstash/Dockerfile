FROM docker.elastic.co/logstash/logstash:8.5.1
RUN rm -f /usr/share/logstash/pipeline/logstash.conf
COPY pipeline/ /usr/share/logstash/pipeline/
EXPOSE 5044
EXPOSE 8080
