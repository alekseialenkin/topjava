package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
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

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    private final Repository storage = new StorageInMemoryMeal();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<MealTo> meals = MealsUtil.to(storage.getAll(), MealsUtil.CALORIES_PER_DAY);
        String action = req.getParameter("action");
        if (action == null) {
            log.debug("Redirect to meals");
            req.setAttribute("meals", meals);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }
        Meal meal1;
        switch (action) {
            case "delete":
                int id = Integer.parseInt(req.getParameter("id"));
                log.debug("Delete " + storage.get(id));
                storage.delete(id);
                resp.sendRedirect("meals");
                return;
            case "edit":
                int id1 = Integer.parseInt(req.getParameter("id"));
                log.debug("Edit " + storage.get(id1));
                meal1 = storage.get(id1);
                break;
            case "new":
                meal1 = new Meal(LocalDateTime.now().withSecond(0).withNano(0), "", 0);
                log.debug("New meal " + meal1);
                break;
            default:
                log.debug("Illegal action");
                return;
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
