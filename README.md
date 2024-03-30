# F2P StarHunt

This repository contains the code for the F2P StarHunt back-end server.
Join our OSRS friends-chat 'F2P StarHunt' or our [Discord](https://discord.gg/XcJBAUzgSt)!


## Running the server in development.

Prerequisites:
- JDK 21, Docker & Docker Compose and a Java IDE such as IntelliJ IDEA or Eclipse IDE.

Steps:
1. Fire up the database. Use the following docker-compose.yaml file:
```yaml
version: '3.9'

services:
  f2p-starhunt-db:
    image: postgres:16-alpine
    container_name: f2p-starhunt-postgres
    restart: always
    environment:
      # See: https://hub.docker.com/_/postgres
      POSTGRES_USER: 'f2p-starhunt'
      POSTGRES_PASSWORD: 'change_me'
      POSTGRES_DB: 'f2p-starhunt'
    ports:
      - "5432:5432"
    volumes:
      - ./.dev/f2p-starhunt-volume:/var/lib/postgresql/data
```

```sh
docker compose up -d
```

2. Initialise the database.

Connect to the database and execute the script `db-scripts/setup.sql`.
(only needed once).

3. Run the back-end.
- Install the back-end java dependencies:
```sh
mvnw clean install
```
- Create a run configuration in your IDE which launches the following java class:
com.f2pstarhunt.stars.StarsApplication.
- In the run configuration set the POSTGRES_USER and POSTGRES_PASSWORD so the back-end can connect to the database.
- Execute the run configuration.
- Profit!!

4. To shut down the database, run `docker compose down`.
