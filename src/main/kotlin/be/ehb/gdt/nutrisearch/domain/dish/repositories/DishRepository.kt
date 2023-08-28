package be.ehb.gdt.nutrisearch.domain.dish.repositories

import be.ehb.gdt.nutrisearch.domain.dish.entities.Dish
import be.ehb.gdt.nutrisearch.domain.product.entities.Product

interface DishRepository {
    fun findAllDishesByOwnerId(ownerId: String): List<Dish>
    fun findDishById(id: String): Dish?
    fun findDishByIdAndOwnerId(id: String, ownerId: String): Dish?
    fun saveDish(dish: Dish): Dish
    fun findCompleteProducts(id: String): List<Product>
    fun deleteDish(id: String)
    fun existsById(id: String): Boolean
    fun belongsToUserInfoId(id: String, ownerId: String): Boolean
}