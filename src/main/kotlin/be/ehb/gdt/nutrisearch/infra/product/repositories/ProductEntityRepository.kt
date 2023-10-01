package be.ehb.gdt.nutrisearch.infra.product.repositories

import be.ehb.gdt.nutrisearch.infra.product.entities.ProductEntity

interface ProductEntityRepository {
    fun findAllProducts(): List<ProductEntity>
    fun findAllProductsByOwnerId(ownerId: String): List<ProductEntity>
    fun findProductById(id: String): ProductEntity?
    fun saveProduct(product: ProductEntity): ProductEntity
    fun insertProducts(products: List<ProductEntity>)
    fun verifyProduct(id: String)
    fun deleteProductById(id: String)
    fun existsProductById(id: String): Boolean
    fun belongsProductToOwnerId(id: String, ownerId: String): Boolean
    fun findFavoriteProductsByAuthId(authId: String): List<ProductEntity>
}