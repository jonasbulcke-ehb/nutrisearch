package be.ehb.gdt.nutrisearch.domain.product.repositories

import be.ehb.gdt.nutrisearch.domain.product.entities.Product

interface ProductRepository {
    fun findAllProducts(): List<Product>
    fun findProductById(id: String): Product?
    fun saveProduct(product: Product): Product
    fun verifyProduct(id: String)
    fun deleteProductById(id: String)
    fun existProductById(id: String): Boolean
    fun belongProductToOwnerId(id: String, ownerId: String): Boolean
}