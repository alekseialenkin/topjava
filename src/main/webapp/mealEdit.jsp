<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.format.DateTimeFormatterBuilder" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit</title>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo" scope="request"/>
</head>
<body>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${meal.id}">
    <span>DateTime: </span>
    <input type="datetime-local" name="date" value="${meal.dateTime}">
    <br>
    <span>Description: </span>
    <input type="text" name="desc" size="30" value="${meal.description}">
    <br>
    <span>Calories: </span>
    <input type="text" name="cal" size="30" value="${meal.calories}">
    <br>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()" type="reset">Отменить</button>
</form>
</body>
</html>
