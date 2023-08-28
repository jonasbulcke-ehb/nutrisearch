package be.ehb.gdt.nutrisearch.domain.product.repositories

import be.ehb.gdt.nutrisearch.domain.product.entities.Product

interface ProductRepository {
    fun findAllProducts(): List<Product>
    fun findAllProductsByOwnerId(ownerId: String): List<Product>
    fun findProductById(id: String): Product?
    fun saveProduct(product: Product): Product
    fun insertProducts(products: List<Product>)
    fun verifyProduct(id: String)
    fun deleteProductById(id: String)
    fun existsProductById(id: String): Boolean
    fun belongsProductToOwnerId(id: String, ownerId: String): Boolean
    fun findFavoriteProductsByAuthId(authId: String): List<Product>
}