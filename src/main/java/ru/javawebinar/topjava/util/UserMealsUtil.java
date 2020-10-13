package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toList;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        List<UserMealWithExcess> meals2To = getFilteredWithExceededInOnePass2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
       // meals2To.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        return meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate())).values()
                .stream()
                .flatMap(mealList -> {
                    int sum = mealList.stream().mapToInt(UserMeal::getCalories).sum();
                    boolean isExceed = sum > caloriesPerDay;

                    return mealList.stream()
                            .filter(timeOfMeal -> TimeUtil.isBetweenHalfOpen(timeOfMeal.getDateTime().toLocalTime(), startTime, endTime))
                            .map(newMeal -> new UserMealWithExcess(newMeal.getDateTime(), newMeal.getDescription(), newMeal.getCalories(), isExceed));


                }).collect(toList());

    }

    public static List<UserMealWithExcess> getFilteredWithExceededInOnePass2(Collection<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final class Aggregate {
            private final List<UserMeal> dailyMeals = new ArrayList<>();
            private int dailySumOfCalories;

            private void accumulate(UserMeal meal) {
                dailySumOfCalories += meal.getCalories();
                if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    dailyMeals.add(meal);
                }
            }

            // never invoked if the upstream is sequential
            private Aggregate combine(Aggregate that) {
                this.dailySumOfCalories += that.dailySumOfCalories;
                this.dailyMeals.addAll(that.dailyMeals);
                return this;
            }

            private Stream<UserMealWithExcess> finisher() {
                final boolean exceed = dailySumOfCalories > caloriesPerDay;
                return dailyMeals.stream().map(meal -> createWithExceed(meal, exceed));
            }
        }

//        Map<LocalDateTime, List<UserMeal>> collect = meals.stream()
//                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime()));

//        for(Map.Entry<LocalDateTime, List<UserMeal>> item : collect.entrySet()){
//
//            System.out.println(item.getKey());
//            for(UserMeal userMeal : item.getValue()){
//                System.out.println(userMeal.getDescription() + " "+ userMeal.getDateTime());
//            }
//            System.out.println();
//        }
//        List<List<UserMeal>> v = collect.values().stream().collect(Collectors.toList());
//        v.forEach((x)-> x.forEach((c)-> System.out.println(c.getDateTime() + " " + c.getCalories())));

        Collection<Stream<UserMealWithExcess>> values = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDescription(),
                        Collector.of(() -> new Aggregate(), (aggregate, meal) -> aggregate.accumulate(meal), (aggregate1, that) -> aggregate1.combine(that), aggregate2 -> aggregate2.finisher()))
                ).values();

        return values.stream().flatMap(identity()).collect(toList());
    }

    public static UserMealWithExcess createWithExceed(UserMeal meal, boolean exceeded) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }



    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
