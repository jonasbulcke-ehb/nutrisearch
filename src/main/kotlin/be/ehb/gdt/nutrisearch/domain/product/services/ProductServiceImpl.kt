package be.ehb.gdt.nutrisearch.domain.product.services

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

    override fun createProduct(product: Product) = repo.saveProduct(product)

    override fun updateProduct(id: String, product: Product) {
        if(!repo.existProductById(id)) {
            throw ProductNotFoundException(id)
        }

        if(product.id != id) {
            throw ResourceDoesNotMatchIdException(product.id, id)
        }

        repo.saveProduct(product)
    }

    override fun deleteProduct(id: String) {
        if(!repo.existProductById(id)) {
            throw ProductNotFoundException(id)
        }

        repo.deleteProductById(id)
    }
}