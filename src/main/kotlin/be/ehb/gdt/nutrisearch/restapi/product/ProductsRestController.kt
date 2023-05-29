package be.ehb.gdt.nutrisearch.restapi.product

import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.services.ProductService
import be.ehb.gdt.nutrisearch.restapi.auth.config.RequiresDietitianRole
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductsRestController(private val service: ProductService, private val authFacade: AuthenticationFacade) {

    @GetMapping
    fun getProducts() = service.getProducts()

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: String) = service.getProduct(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postProduct(@RequestBody product: Product): Product {
        val isVerified = authFacade.isInRole("dietitian")
        val ownerId = authFacade.authentication.name
        return service.createProduct(isVerified, ownerId, product)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putProduct(@PathVariable id: String, @RequestBody product: Product) {
        val isVerified = authFacade.isInRole("dietitian")
        val ownerId = authFacade.authentication.name
        service.updateProduct(id, isVerified, ownerId, product)
    }

    @PatchMapping("/{id}/verify")
    @RequiresDietitianRole
    fun verifyProduct(@PathVariable id: String) = service.verifyProduct(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProduct(@PathVariable id: String) = service.deleteProduct(id, authFacade.authentication.name)
}