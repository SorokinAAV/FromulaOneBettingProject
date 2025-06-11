# Formula 1 betting project

Formula 1 betting backend application that will expose a REST HTTP API.

## Getting started

Download the project, build with gradle-wrapper and run the Application.class file in the root directory.

## Available endpoints
- GET `/api/v1/f1/sessions`: Returns all F1 sessions. example: curl -X GET http://localhost:8080/api/v1/f1/sessions?sessionType=Race&country=Belgium (use filtering, because open api restricts the number of requests to 10)
- POST `http://localhost:8080/api/v1/f1/bet`: creates a new bet from user. example: curl -X POST -H "Content-Type: application/json" -d '{
  "userId": "123",
  "sessionKey":"9140",
  "driverId": "1",
  "amount": "60"
  }' http://localhost:8080/api/v1/f1/bet
- POST `http://localhost:8080/api/v1/f1/events/outcome`: starts a new event outcome. example: curl -X POST -H "Content-Type: application/json" -d '{
  "sessionKey": "9140",
  "winningDriverId": "1"
  }' http://localhost:8080/api/v1/f1/events/outcome
- GET `http://localhost:8080/api/v1/f1/bets`: Returns all bets for a user. example: curl -X GET http://localhost:8080/api/v1/f1/bets?userId=123
