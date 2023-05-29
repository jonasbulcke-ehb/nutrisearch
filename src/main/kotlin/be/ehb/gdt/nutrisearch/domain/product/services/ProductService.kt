package be.ehb.gdt.nutrisearch.domain.product.services

import be.ehb.gdt.nutrisearch.domain.product.entities.Product

interface ProductService {
    fun getProducts(): List<Product>
    fun getProduct(id: String): Product
    fun createProduct(isVerified: Boolean, ownerId: String, product: Product): Product
    fun updateProduct(id: String, isVerified: Boolean, ownerId: String, product: Product)
    fun verifyProduct(id: String)
    fun deleteProduct(id: String, ownerId: String)
}