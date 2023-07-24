package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository storage;
    private Collection<MealTo> meals;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new InMemoryMealRepository();
        meals = MealsUtil.to(storage.getAll(), MealsUtil.CALORIES_PER_DAY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        meals = MealsUtil.to(storage.getAll(), MealsUtil.CALORIES_PER_DAY);
        String action = req.getParameter("action");
        if (action == null) {
            log.debug("Redirect to meals");
            req.setAttribute("meals", meals);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            return;
        }
        switch (action) {
            case "delete": {
                int id = Integer.parseInt(req.getParameter("id"));
                log.debug("Delete meal with id {}", id);
                storage.delete(id);
                resp.sendRedirect("meals");
                return;
            }
            case "edit": {
                int id = Integer.parseInt(req.getParameter("id"));
                Meal meal = storage.get(id);
                log.debug("Edit meal with id {}", id);
                req.setAttribute("meal", meal);
                break;
            }
            case "new": {
                Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0);
                log.debug("New meal {}", meal);
                req.setAttribute("meal", meal);
                break;
            }
            default:
                log.debug("Illegal action");
                resp.sendRedirect("meals");
                return;
        }

        req.getRequestDispatcher("/mealEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));
        String description = req.getParameter("desc");
        int calories = Integer.parseInt(req.getParameter("cal"));
        Meal m = new Meal(date, description, calories);
        if (req.getParameter("id").isEmpty()) {
            log.debug("Create new meal");
            storage.create(m);
        } else {
            int id = Integer.parseInt(req.getParameter("id"));
            log.debug("Update meal with id {}", id);
            m.setId(id);
            storage.update(m);
        }
        resp.sendRedirect("meals");
    }
}
