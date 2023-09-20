package be.ehb.gdt.nutrisearch.domain.product.services

import be.ehb.gdt.nutrisearch.domain.exceptions.ForbiddenOperationException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.repositories.ProductRepository
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.excel.ProductsExcelReader
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class ProductServiceImpl(
    private val repo: ProductRepository,
    private val userInfoRepo: UserInfoRepository,
    private val authFacade: AuthenticationFacade
) : ProductService {
    override fun getProducts(ownProducts: Boolean) =
        if (ownProducts) repo.findAllProductsByOwnerId(getUserInfoId()) else repo.findAllProducts()

    override fun getProduct(id: String) =
        repo.findProductById(id) ?: throw ResourceNotFoundException(Product::class.java, id)

    override fun createProduct(product: Product): Product {
        return product.apply {
            servingSizes.add(ServingSize())
            isVerified = authFacade.isInRole("dietitian")
            ownerId = getUserInfoId()
        }.also {
            repo.saveProduct(it)
        }
    }

    override fun updateProduct(id: String, product: Product) {
        if (!repo.existsProductById(id)) {
            throw ResourceNotFoundException(Product::class.java, id)
        }

        val ownerId = getUserInfoId()
        val isVerified = authFacade.isInRole("dietitian")

        if (!repo.belongsProductToOwnerId(id, ownerId) && !isVerified) {
            throw ForbiddenOperationException("Forbidden to modify product with id $id")
        }

        if (product.id != id) {
            throw ResourceDoesNotMatchIdException(product.id, id)
        }

        product.apply {
            this.servingSizes.add(ServingSize())
            this.isVerified = isVerified
            this.ownerId = ownerId
        }.also {
            repo.saveProduct(it)
        }
    }

    override fun verifyProduct(id: String) {
        repo.verifyProduct(id)
    }

    override fun deleteProduct(id: String) {
        if (!repo.existsProductById(id)) {
            throw ResourceNotFoundException(Product::class.java, id)
        }

        if (!repo.belongsProductToOwnerId(id, getUserInfoId()) && !authFacade.isInRole("dietitian")) {
            throw ForbiddenOperationException("Forbidden to delete product with id $id")
        }

        repo.deleteProductById(id)
    }

    override fun importProducts(categoryId: String, isVerified: Boolean, inputStream: InputStream) {
        ProductsExcelReader
            .from(inputStream)
            .witProductDetails(categoryId, isVerified && authFacade.isInRole("dietitian"), ServingSize())
            .readProducts()
            .also { repo.insertProducts(it) }
    }

    override fun getFavoriteProducts() = repo.findFavoriteProductsByAuthId(authFacade.authId)

    private fun getUserInfoId() =
        userInfoRepo.findUserInfoIdByAuthId(authFacade.authentication.name) ?: throw NoUserInfoForAuthenticationFound()
}