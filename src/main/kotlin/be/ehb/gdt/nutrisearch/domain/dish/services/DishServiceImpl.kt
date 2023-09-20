package be.ehb.gdt.nutrisearch.domain.dish.services

import be.ehb.gdt.nutrisearch.domain.dish.entities.Dish
import be.ehb.gdt.nutrisearch.domain.dish.repositories.DishRepository
import be.ehb.gdt.nutrisearch.domain.exceptions.ForbiddenOperationException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceDoesNotMatchIdException
import be.ehb.gdt.nutrisearch.domain.exceptions.ResourceNotFoundException
import be.ehb.gdt.nutrisearch.domain.product.entities.Product
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserinfoDeletedEvent
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class DishServiceImpl(
    private val dishRepo: DishRepository,
    private val userInfoRepo: UserInfoRepository,
    private val authFacade: AuthenticationFacade
) : DishService {
    private val userInfoId: String by lazy {
        userInfoRepo.findUserInfoIdByAuthId(authFacade.authId) ?: throw NoUserInfoForAuthenticationFound();
    }

    override fun getDishes(): List<Dish> {
        return dishRepo.findAllDishesByOwnerId(userInfoId)
    }

    override fun getDish(id: String): Dish {
        val dish = dishRepo.findDishById(id) ?: throw ResourceNotFoundException("Dish", id)
        if (dish.ownerId != userInfoId) {
            throw ForbiddenOperationException("Forbidden to retrieve dish with id $id")
        }
        return dish
    }

    override fun createDish(dish: Dish) = dish.apply {
        ownerId = userInfoId
    }.also {
        dishRepo.saveDish(it)
    }

    override fun updateDish(id: String, dish: Dish) {
        if (dish.id != id) {
            throw ResourceDoesNotMatchIdException(dish.id, id)
        }

        if (!dishRepo.existsById(id)) {
            throw ResourceNotFoundException("Consumption", id)
        }

        if (!dishRepo.belongsToUserInfoId(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to modify dish with id $id")
        }

        dish.apply {
            ownerId = userInfoId
        }.also {
            dishRepo.saveDish(it)
        }
    }

    override fun deleteDish(id: String) {
        if (!dishRepo.existsById(id)) {
            throw ResourceNotFoundException("Consumption", id)
        }

        if (!dishRepo.belongsToUserInfoId(id, userInfoId)) {
            throw ForbiddenOperationException("Forbidden to delete dish with id $id")
        }

        dishRepo.deleteDish(id)
    }

    override fun getCompleteProducts(id: String): List<Product> {
        if(!dishRepo.existsById(id)) {
            throw ResourceNotFoundException("Consumption", id)
        }

        return dishRepo.findCompleteProducts(id)
    }

    @EventListener(UserinfoDeletedEvent::class)
    fun handleUserinfoDeletedEvent(event: UserinfoDeletedEvent) {
        dishRepo.deleteDishesByUserinfoId(event.userinfoId)
    }


}