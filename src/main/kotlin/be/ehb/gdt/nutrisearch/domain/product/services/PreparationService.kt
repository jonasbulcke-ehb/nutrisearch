package be.ehb.gdt.nutrisearch.domain.product.services

import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation

interface PreparationService {
    fun getPreparations(productId: String): List<Preparation>
    fun getPreparation(productId: String, id: String): Preparation
    fun createPreparation(productId: String, preparation: Preparation): Preparation
    fun updatePreparation(productId: String, id: String, preparation: Preparation)
    fun deletePreparation(productId: String, id: String)
}
