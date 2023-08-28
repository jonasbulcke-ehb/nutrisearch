package be.ehb.gdt.nutrisearch.restapi.dishes

import be.ehb.gdt.nutrisearch.domain.dish.entities.Dish
import be.ehb.gdt.nutrisearch.domain.dish.services.DishService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/dishes")
class DishesController(private val service: DishService) {

    @GetMapping
    fun getDishes() = service.getDishes()

    @GetMapping("/{id}")
    fun getDish(@PathVariable id: String) = service.getDish(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postDish(@RequestBody dish: Dish) = service.createDish(dish)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putDish(@PathVariable id: String, @RequestBody dish: Dish) = service.updateDish(id, dish)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDish(@PathVariable id: String) = service.deleteDish(id)

    @GetMapping("/{id}/complete-products")
    fun getPreparations(@PathVariable id: String) = service.getCompleteProducts(id)
}
