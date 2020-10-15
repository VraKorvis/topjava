package ru.javawebinar.topjava.DAO;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDAO {

    Meal get(int id);
    Meal addOrUpdate(Meal meal);
    void delete(int id);
    Collection<Meal> getAll();
}
