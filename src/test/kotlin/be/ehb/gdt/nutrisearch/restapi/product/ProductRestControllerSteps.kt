package be.ehb.gdt.nutrisearch.restapi.product

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.ServingSize
import be.ehb.gdt.nutrisearch.restapi.SpringIntegrationTest
import io.cucumber.java.DataTableType
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

class ProductRestControllerSteps : SpringIntegrationTest() {
    @DataTableType
    fun consumptionsTransformer(row: Map<String, String?>): Product {
        return Product(
            null,
            row["name"]!!,
            row["categoryId"]!!,
            row["isVerified"]!!.toBooleanStrict(),
            mutableListOf(Preparation("Onbereid")),
            mutableSetOf(ServingSize()),
            row["id"]!!
        ).apply {
            ownerId = row["ownerId"]!!
        }
    }

    @Given("the following products")
    fun theFollowingProducts(products: List<Product>) = products.forEach(productRepository::saveProduct)

    @And("I expect the response body to contain a verified product")
    fun iExpectTheResponseBodyToContainAVerifiedProduct() {
        testContext.resultActions!!.andExpect(content().json("""{"isVerified": true}"""))
    }

    @And("I expect the response body to contain a unverified product")
    fun iExpectTheResponseBodyToContainAUnverifiedProduct() {
        testContext.resultActions!!.andExpect(content().json("""{"isVerified": false}"""))
    }
}