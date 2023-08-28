package be.ehb.gdt.nutrisearch.domain.dish.services

import be.ehb.gdt.nutrisearch.domain.dish.entities.Dish
import be.ehb.gdt.nutrisearch.domain.product.entities.Product

interface DishService {
    fun getDishes(): List<Dish>
    fun getDish(id: String): Dish
    fun createDish(dish: Dish): Dish
    fun updateDish(id: String, dish: Dish)
    fun deleteDish(id: String)
    fun getCompleteProducts(id: String): List<Product>

}