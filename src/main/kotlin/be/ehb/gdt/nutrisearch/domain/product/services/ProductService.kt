package be.ehb.gdt.nutrisearch.domain.product.services

import be.ehb.gdt.nutrisearch.domain.product.entities.Product

interface ProductService {
    fun getProducts(): List<Product>
    fun getProduct(id: String): Product
    fun createProduct(product: Product): Product
    fun updateProduct(id: String, product: Product)
    fun deleteProduct(id: String)
}