<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Meal</title>
</head>
<body>
<h3>${meal.id==null?'Add meal':'Edit meal'}</h3>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="id" value="${meal.id}">
    <span>DateTime: </span>
    <input type="datetime-local" name="date" value="${meal.dateTime}">
    <br>
    <span>Description: </span>
    <input type="text" name="desc" size="30" value="${meal.description}">
    <br>
    <span>Calories: </span>
    <input type="number" name="cal" size="30" value="${meal.calories}">
    <br>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()" type="reset">Отменить</button>
</form>
</body>
</html>
