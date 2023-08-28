package be.ehb.gdt.nutrisearch.restapi.userinfo

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.services.UserInfoService
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/userinfo")
class UserInfoRestController(private val service: UserInfoService) {
    @GetMapping
    fun getAuthorizedUserInfo() = service.getAuthenticatedUserInfo()

    @GetMapping("/has-userinfo")
    fun hasAuthenticationLinkedUserInfo() = service.hasAuthenticationLinkedUserInfo()

    @GetMapping("/current-study")
    fun getCurrentStudy() = service.getAuthenticationCurrentStudy()

    @GetMapping("/treatment-team")
    fun getTreatmentTeam() = service.getAuthenticationTreatmentTeam()

    @PostMapping("/treatment-team/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addToTreatmentTeam(@PathVariable id: String) = service.addToTreatmentTeam(id)

    @DeleteMapping("/treatment-team/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFromTreatmentTeam(@PathVariable id: String) = service.deleteFromTreatmentTeam(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postUserInfo(@RequestBody userInfo: UserInfo) = service.createUserInfo(userInfo)

    @PatchMapping
    fun patchUserInfo(@RequestBody userUpdatableInfo: UserUpdatableInfo) = service.updateUserInfo(userUpdatableInfo)

    @PostMapping("/weight")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun postWeightMeasurement(@RequestBody weightMeasurement: WeightMeasurement) =
        service.addWeightMeasurement(weightMeasurement)

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserInfo() = service.deleteUserInfoByAuthId()

    @GetMapping("/favorite-products")
    fun getFavoriteProductIds() = service.getAuthenticatedUserInfo().favoriteProductIds


    @PostMapping("/favorite-products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun postFavoriteProduct(@PathVariable productId: String) = service.addFavoriteProduct(productId)

    @DeleteMapping("/favorite-products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFavoriteProduct(@PathVariable productId: String) = service.deleteFavoriteProduct(productId)

    @GetMapping("/questionnaire/{date}")
    fun getQuestionnaire(@PathVariable date: LocalDate) = service.getQuestionnaire(date)

    @PutMapping("/questionnaire/{date}/answers/{answerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putAnswer(@PathVariable date: LocalDate, @PathVariable answerId: String, @RequestBody answer: String) {
        service.addAnswerToQuestionnaire(date, answerId, answer)
    }
}
