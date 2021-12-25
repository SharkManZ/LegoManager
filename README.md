# Сборка docker образа
docker build -t lego/manager .
# запуск контейнера
docker run -itd -p 8085:8085 --name=lego-manager  --network=lego-net lego/manager