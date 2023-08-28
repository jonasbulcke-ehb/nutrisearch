package be.ehb.gdt.nutrisearch.domain.consumption.valueobjects

import be.ehb.gdt.nutrisearch.domain.dish.entities.Dish
import java.time.LocalDate

data class DishConsumption(val dish: Dish, val amount: Double, val meal: Meal, val timestamp: LocalDate)