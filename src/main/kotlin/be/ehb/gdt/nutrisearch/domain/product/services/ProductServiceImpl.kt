package be.ehb.gdt.nutrisearch.domain.product.services

import be.ehb.gdt.nutrisearch.domain.exceptions.ForbiddenOperationException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.exceptions.ProductNotFoundException
import be.ehb.gdt.nutrisearch.domain.product.repositories.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(val repo: ProductRepository) : ProductService {
    override fun getProducts() = repo.findAllProducts()

    override fun getProduct(id: String): Product {
        return repo.findProductById(id) ?: throw ProductNotFoundException(id)
    }

    override fun createProduct(isVerified: Boolean, ownerId: String, product: Product) =
        product.apply {
            this.isVerified = isVerified
            this.ownerId = ownerId
        }.also {
            repo.saveProduct(it)
        }

    override fun updateProduct(id: String, isVerified: Boolean, ownerId: String, product: Product) {
        if (!repo.existsProductById(id)) {
            throw ProductNotFoundException(id)
        }

        if (!repo.belongsProductToOwnerId(id, ownerId) && !isVerified) {
            throw ForbiddenOperationException("Forbidden to modify product with id $id")
        }

        if (product.id != id) {
            throw ResourceDoesNotMatchIdException(product.id, id)
        }

        product.apply {
            this.isVerified = isVerified
            this.ownerId = ownerId
        }.also {
            repo.saveProduct(it)
        }
    }

    override fun verifyProduct(id: String) {
        repo.verifyProduct(id)
    }

    override fun deleteProduct(id: String, ownerId: String) {
        if (!repo.existsProductById(id)) {
            throw ProductNotFoundException(id)
        }

        if (!repo.belongsProductToOwnerId(id, ownerId)) {
            throw ForbiddenOperationException("Forbidden to delete product with id $id")
        }

        repo.deleteProductById(id)
    }
}