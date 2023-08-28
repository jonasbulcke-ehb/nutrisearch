package be.ehb.gdt.nutrisearch.restapi.product

import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.services.ProductService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/products")
class ProductsRestController(private val service: ProductService) {

    @GetMapping
    fun getProducts(@RequestParam(defaultValue = "false") ownProducts: Boolean) = service.getProducts(ownProducts)

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

    @PostMapping("/import-excel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun importProducts(
        @RequestParam(name = "category") categoryId: String,
        @RequestParam(defaultValue = "false") isVerified: Boolean,
        @RequestParam("products-excel-file") file: MultipartFile,
        request: HttpServletRequest
    ) {
        service.importProducts(categoryId, isVerified, file.inputStream)
    }

    @GetMapping("/favorites")
    fun getFavoriteProducts() = service.getFavoriteProducts()
}