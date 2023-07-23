package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.StorageInMemoryMeal;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.Repository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

public class MealServlet extends HttpServlet {
    private final Repository storage = new StorageInMemoryMeal();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<MealTo> meal = MealsUtil.to(storage.getAll(), MealsUtil.CALORIES_PER_DAY);
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("meals", meal);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }
        Meal meal1;
        switch (action) {
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                storage.delete(id);
                resp.sendRedirect("meals");
                return;
            case "edit":
                int id1 = Integer.parseInt(req.getParameter("id"));
                meal1 = storage.get(id1);
                break;
            case "new":
                meal1 = new Meal(LocalDateTime.now().withSecond(0).withNano(0), "", 0);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }
        req.setAttribute("meal", meal1);
        req.getRequestDispatcher("/mealEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));
        String description = req.getParameter("desc");
        int calories = Integer.parseInt(req.getParameter("cal"));
        Meal m = new Meal(date, description, calories);
        if (req.getParameter("id").trim().length() == 0) {
            storage.save(m);
        } else {
            Integer id = Integer.parseInt(req.getParameter("id"));
            m.setId(id);
            storage.update(m);
        }
        resp.sendRedirect("meals");
    }
}
