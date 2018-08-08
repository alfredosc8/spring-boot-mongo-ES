FROM logstash:5.5.2

COPY logstash.conf /config-dir/
EXPOSE 443
CMD ["-f", "/config-dir/logstash.conf"]
