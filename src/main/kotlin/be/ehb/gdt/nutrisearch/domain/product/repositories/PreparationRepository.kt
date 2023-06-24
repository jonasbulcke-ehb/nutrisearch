package be.ehb.gdt.nutrisearch.domain.product.repositories

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation

interface PreparationRepository {
    fun getPreparations(productId: String): List<Preparation>?
    fun getPreparation(productId: String, id: String): Preparation?
    fun insertPreparation(productId: String, preparation: Preparation): Preparation
    fun updatePreparation(productId: String, id: String, preparation: Preparation)
    fun deletePreparation(productId: String, id: String)
    fun existsProductById(productId: String): Boolean
    fun existsPreparationsById(productId: String, id: String): Boolean
    fun belongsToOwnerId(productId: String, ownerId: String): Boolean
}