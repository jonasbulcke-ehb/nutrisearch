package be.ehb.gdt.nutrisearch.domain.product.services

import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.product.entities.Preparation
import be.ehb.gdt.nutrisearch.domain.product.repositories.PreparationRepository
import org.springframework.stereotype.Service

@Service
class PreparationServiceImpl(private val repo: PreparationRepository) : PreparationService {
    override fun getPreparations(productId: String): List<Preparation> {
        if (!repo.existsProductById(productId)) {
            throw ResourceNotFoundException("Product", productId)
        }

        return repo.getPreparations(productId) ?: emptyList()
    }

    override fun getPreparation(productId: String, id: String): Preparation {
        if (!repo.existsProductById(productId)) {
            throw ResourceNotFoundException("Product", productId)
        }

        return repo.getPreparation(productId, id) ?: throw ResourceNotFoundException(Preparation::class.java, id)
    }

    override fun createPreparation(productId: String,  preparation: Preparation): Preparation {
        if (!repo.existsProductById(productId)) {
            throw ResourceNotFoundException("Product", productId)
        }

        return repo.insertPreparation(productId, preparation)
    }

    override fun updatePreparation(productId: String, id: String, preparation: Preparation) {
        if(preparation.id != id) {
            throw ResourceDoesNotMatchIdException(preparation.id, id)
        }

        if(!repo.existsPreparationsById(productId, id)) {
            throw ResourceNotFoundException(Preparation::class.java, id)
        }

        repo.updatePreparation(productId, id, preparation)
    }

    override fun deletePreparation(productId: String, id: String) {
        if(!repo.existsPreparationsById(productId, id)) {
            throw ResourceNotFoundException(Preparation::class.java, id)
        }

        repo.deletePreparation(productId, id)
    }
}