# School

## Build and Run
```shell
# api - build and run tests
./gradlew build

# docker containers - build
make build

# docker containers - up
make up

# docker containers - down
make down
```
## Swagger

http://localhost:8080/swagger-ui/

```shell
export TOKEN=$(curl -s \
  'http://keycloak:11000/realms/school/protocol/openid-connect/token' \
  --data-urlencode 'username=admin1' \
  --data-urlencode "password=$KC_DEFAULT_USER_PASSWORD" \
  --data-urlencode "client_secret=$KC_SCHOOL_API_CLIENT_SECRET" \ 
  -d 'client_id=school-api' \
  -d 'grant_type=password' \
  -d 'scope=openid' \
  | jq -j .access_token) && echo $TOKEN | pbcopy
```

## API Call Example

```shell
curl -X 'POST' 'http://localhost:8080/v1/course' -d '{"name":"Linear Algebra","level":"IGCSE","subject":"Maths"}' -H "Authorization: Bearer $TOKEN"
```
