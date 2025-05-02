FROM alpine:latest

RUN apk add --no-cache curl

WORKDIR /app

# Создаем скрипт
RUN echo '#!/bin/sh\n\
while true; do\n\
    echo "Fetching dishes..."\n\
    curl -s http://localhost:30113/dishes\n\
    echo "\n-------------------"\n\
    sleep 5\n\
done' > /app/check-dishes.sh

RUN chmod +x /app/check-dishes.sh

CMD ["/app/check-dishes.sh"]