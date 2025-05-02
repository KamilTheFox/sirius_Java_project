FROM alpine:latest

RUN apk add --no-cache curl

# Создаем директорию
WORKDIR /app

# Создаем скрипт и сразу делаем его исполняемым
COPY <<'EOF' /app/check-dishes.sh
#!/bin/sh
while true; do
    echo "Fetching dishes..."
    curl -s http://main.var13.svc.cluster.local:8080/dishes
    echo "\n-------------------"
    sleep 10
done
EOF

RUN chmod +x /app/check-dishes.sh

CMD ["/app/check-dishes.sh"]