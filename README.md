Spring Boot WebFlux service, runs with Netty with `gradle :bootRun`

Requires postgres database to start, you can run one in docker with
```
docker run --name weatherdb -e POSTGRES_PASSWORD=secretpass -e POSTGRES_USER=weather -p 5432:5432 -d postgres:14.1-alpine
```

After startp serves on [localhost:8080/weather](http://localhost:8080/weather), 
you could also provide ip address with 
[localhost:8080/weather?ip=your.ip.address](http://localhost:8080/weather?ip=8.8.8.8)