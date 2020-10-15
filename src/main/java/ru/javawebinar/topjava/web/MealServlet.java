package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.DAO.MealDAO;
import ru.javawebinar.topjava.DAO.MealDAOImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private static final long serialVersionUID = 1L;
    private final static String LIST_MEALS = "/meals.jsp";
    private final static String EDIT_OR_ADD = "/edit.jsp";
    private MealDAOImpl mealsDao;

    @Override
    public void init() throws ServletException {
        super.init();
        this.mealsDao = new MealDAOImpl();
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LOG.debug("redirect to meals");
        request.setCharacterEncoding("UTF-8");

        String forward="";
        final String action = request.getParameter("action");

        if (action == null) {
            forward = LIST_MEALS;
            setAttrListOfMeals(request);
            request.getRequestDispatcher(forward).forward(request, response);
        } else {
            switch (action.toLowerCase()) {
                case "delete":
                    int id = Integer.parseInt(request.getParameter("id"));
                    mealsDao.delete(id);
                    break;
                case "edit":
                    id = Integer.parseInt(request.getParameter("id"));
                    Meal meal = mealsDao.get(id);
                    request.setAttribute("meal", meal);
                case "add":
                    forward = EDIT_OR_ADD;
                    request.getRequestDispatcher(forward).forward(request, response);
                    break;
            }
        }
        response.sendRedirect("meals.jsp");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("redirect to Add or Edit");
        request.setCharacterEncoding("UTF-8");

        final String description = request.getParameter("description");
        final int calories = Integer.parseInt(request.getParameter("calories"));
//      final String dateAndTime = request.getParameter("date") + " " + request.getParameter("time");
        final String dateAndTime = request.getParameter("dateTime");
        System.out.println(dateAndTime);

        final LocalDateTime dateTime = LocalDateTime.parse(dateAndTime, formatter);

        Meal newMeal = new Meal(0, dateTime, description, calories);
        final String id = request.getParameter("id");
        if (!id.equals("")) {
            newMeal.setId(Integer.parseInt(id));
        }
        mealsDao.addOrUpdate(newMeal);

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        setAttrListOfMeals(request);
        view.forward(request, response);
    }

    private void setAttrListOfMeals(HttpServletRequest request) {
        List<MealTo> mealTo = MealsUtil.filteredByStreams(mealsDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("mealsTo", mealTo);
    }
}
