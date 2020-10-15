package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MealTo {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;
    private int id;


    //    private final AtomicBoolean excess;      // filteredByAtomic (or Boolean[])
//    private final Boolean excess;            // filteredByReflection
//    private final Supplier<Boolean> excess;  // filteredByClosure
    private boolean excess;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

//    public Boolean getExcess() {
//        return excess;
//    }

    // for filteredBySetterRecursion
    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

}
