package be.ehb.gdt.nutrisearch.excel

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.product.valueobjects.Nutrient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.util.ResourceUtils

class ProductsExcelReaderTest {
    @Test
    fun test() {
        val categoryId = "test-category-id"
        val actualProducts =
            ResourceUtils.getFile("classpath:products/producten.xlsx").inputStream()
                .let { ProductsExcelReader.from(it).readProducts() }

        val nutrients1: MutableMap<Nutrient, Double?> =
            Nutrient.values().toMutableList()
                .apply { removeAll(listOf(Nutrient.VitaminE, Nutrient.Folate, Nutrient.Iodine)) }
                .withIndex().associate { it.value to 99.0 - it.index }.toMutableMap()
        listOf(
            Nutrient.VitaminE,
            Nutrient.Folate,
            Nutrient.Iodine,
            Nutrient.Fiber,
            Nutrient.Sodium
        ).forEach { nutrients1[it] = null }

        val nutrients2: MutableMap<Nutrient, Double?> =
            Nutrient.values().toMutableList()
                .apply { removeAll(listOf(Nutrient.VitaminE, Nutrient.Folate, Nutrient.Iodine)) }
                .withIndex().associate { it.value to 50.0 - it.index }.toMutableMap()
        listOf(Nutrient.VitaminE, Nutrient.Folate, Nutrient.Iodine).forEach { nutrients2[it] = null }

        val expectedProducts = listOf(
            Product(
                "Chiquita",
                "Banaan",
                categoryId,
                preparations = mutableListOf(Preparation("Onbereid", nutrients1))
            ),
            Product(
                null,
                "Brood",
                categoryId,
                preparations = mutableListOf(Preparation("Onbereid", nutrients2))
            )
        )

        assertEquals(expectedProducts.size, actualProducts.size)
        expectedProducts.indices.map { expectedProducts[it] to actualProducts[it] }
            .forEach { (expected, actual) -> assertProductEquals(expected, actual) }
    }

    private fun assertProductEquals(expected: Product, actual: Product) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.brand, actual.brand)
        assertEquals(expected.servingSizes, actual.servingSizes)
        val expectedPreparation = expected.preparations.first()
        val actualPreparation = actual.preparations.first()
        assertEquals(expectedPreparation.name, actualPreparation.name)
        for ((nutrient, amount) in expectedPreparation.nutrients) {
            assertTrue(actualPreparation.nutrients.containsKey(nutrient), "Expected to contain $nutrient")
            assertEquals(amount, actualPreparation.nutrients[nutrient], "The amounts of $nutrient does not match")
        }
    }


}