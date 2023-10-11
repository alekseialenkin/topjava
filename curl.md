getAll():
curl -X GET http://localhost:8080/topjava/rest/profile/meals
get():
curl -X GET http://localhost:8080/topjava/rest/profile/meals/{id}
delete():
curl -X DELETE http://localhost:8080/topjava/rest/profile/meals/{id}
create():
curl -X POST -H "Content-Type: application/json" -d '{ "dateTime": "2023-10-11T15:30:00",
 "description": "Lunch", "calories": 500 }' http://localhost:8080/topjava/rest/profile/meals
update():
curl -X PUT -H "Content-Type: application/json" -d '{ "dateTime": "2023-10-11T18:00:00",
 "description": "Dinner", "calories": 700 }' http://localhost:8080/topjava/rest/profile/meals/{id}
getBetween():
curl -X GET "http://localhost:8080/topjava/rest/profile/meals/filter?startDate=2023-10-10&startTime=08:00:00&endDate=2023-10-11&endTime=12:00:00"