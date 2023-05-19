package be.ehb.gdt.nutrisearch.restapi.product

import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.repositories.ProductMongoRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductsController(private val repo: ProductMongoRepository) {

    @GetMapping
    fun getProducts() = repo.findAllProducts()

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: String) = repo.findProductById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postProduct(@RequestBody product: Product) = repo.saveProduct(product)

}