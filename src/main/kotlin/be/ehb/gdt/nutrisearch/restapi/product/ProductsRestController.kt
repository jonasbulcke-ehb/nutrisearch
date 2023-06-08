package be.ehb.gdt.nutrisearch.restapi.product

import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.services.ProductService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductsRestController(private val service: ProductService) {

    @GetMapping
    fun getProducts() = service.getProducts()

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: String) = service.getProduct(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postProduct(@RequestBody product: Product) = service.createProduct(product)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putProduct(@PathVariable id: String, @RequestBody product: Product) = service.updateProduct(id, product)

    @PutMapping("/{id}/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun verifyProduct(@PathVariable id: String) = service.verifyProduct(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: String) = service.deleteProduct(id)
}