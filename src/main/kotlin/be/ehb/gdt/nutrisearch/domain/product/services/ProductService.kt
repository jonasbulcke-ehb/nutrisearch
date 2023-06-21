package be.ehb.gdt.nutrisearch.domain.product.services

import be.ehb.gdt.nutrisearch.domain.product.entities.Product

interface ProductService {
    fun getProducts(ownProducts: Boolean): List<Product>
    fun getProduct(id: String): Product
    fun createProduct(product: Product): Product
    fun updateProduct(id: String, product: Product)
    fun verifyProduct(id: String)
    fun deleteProduct(id: String)
}