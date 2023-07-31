package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private final ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");


    private MealRestController repository;

    @Override
    public void init() {
        repository = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("id") == null) {
            request.setAttribute("meals", repository.getAllSorted(request.getParameter("startDate").isEmpty() ? LocalDate.MIN :
                    LocalDate.parse(request.getParameter("startDate")), request.getParameter("endDate").isEmpty() ? LocalDate.now() :
                    LocalDate.parse(request.getParameter("endDate")), request.getParameter("startTime").isEmpty() ? LocalTime.MIN :
                    LocalTime.parse(request.getParameter("startTime")), request.getParameter("endTime").isEmpty() ? LocalTime.MAX :
                    LocalTime.parse(request.getParameter("endTime")), SecurityUtil.authUserId()));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            String id = request.getParameter("id");
            Meal meal = new Meal(null, id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                repository.create(meal, SecurityUtil.authUserId());
            } else {
                repository.update(meal, Integer.parseInt(id), SecurityUtil.authUserId());
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                repository.delete(id, SecurityUtil.authUserId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        repository.get(getId(request), SecurityUtil.authUserId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        repository.getAll(SecurityUtil.authUserId()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
