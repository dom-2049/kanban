FROM docker.elastic.co/beats/filebeat:8.5.1
COPY --chown=root:filebeat /config/filebeat.yml /usr/share/filebeat/filebeat.yml
COPY --chown=root:filebeat /certificates /certs

