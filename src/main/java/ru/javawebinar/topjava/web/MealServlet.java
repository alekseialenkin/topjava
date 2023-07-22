package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.MealInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

public class MealServlet extends HttpServlet {
    Collection<MealTo> meal;
    MealInMemory m = new MealInMemory();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        meal = MealsUtil.to((List<Meal>) m.getAll(), MealsUtil.CALORIES_PER_DAY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        if (action == null) {
            req.setAttribute("meals", meal);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }
        MealTo meal1;
        switch (action) {
            case "delete":
                m.delete(Integer.parseInt(id));
                meal = MealsUtil.to((List<Meal>) m.getAll(), MealsUtil.CALORIES_PER_DAY);
                resp.sendRedirect("meals");
                return;
            case "edit":
                meal1 = MealsUtil.makeTo(m.get(Integer.parseInt(id)));
                break;
            case "new":
                meal1 = new MealTo(LocalDateTime.now(),"",0,true,0);
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
        Integer id = Integer.parseInt(req.getParameter("id"));
        String date = req.getParameter("date");
        String description = req.getParameter("desc");
        String calories = req.getParameter("cal");
        m.delete(id);
        m.save(new Meal(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")), description, Integer.parseInt(calories)));
        req.setAttribute("meals", MealsUtil.to((List<Meal>) m.getAll(), MealsUtil.CALORIES_PER_DAY));
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
