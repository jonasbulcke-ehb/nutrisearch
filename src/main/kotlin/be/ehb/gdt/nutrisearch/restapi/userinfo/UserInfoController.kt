package be.ehb.gdt.nutrisearch.restapi.userinfo

import be.ehb.gdt.nutrisearch.restapi.auth.config.RequiresDietitianRole
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.services.UserInfoService
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/userinfo")
class UserInfoController(private val service: UserInfoService, private val authenticationFacade: AuthenticationFacade) {
    @GetMapping
    fun getAuthorizedUserInfo() = service.getUserInfoByAuthId(authenticationFacade.authentication.name)

    @GetMapping("/{id}")
    @RequiresDietitianRole
    fun getUserInfo(@PathVariable id: String) = service.getUserInfo(id)

    @GetMapping("/has-userinfo")
    fun hasAuthenticationLinkedUserInfo() = service.hasUserInfoAuthId(authenticationFacade.authentication.name)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postUserInfo(@RequestBody userInfo: UserInfo) =
        service.createUserInfo(authenticationFacade.authentication.name, userInfo)

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchUserInfo(@RequestBody userUpdatableInfo: UserUpdatableInfo) =
        service.updateUserInfo(authenticationFacade.authentication.name, userUpdatableInfo)

    @PostMapping("/weight")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun postWeightMeasurement(@RequestBody weightMeasurement: WeightMeasurement) =
        service.addWeightMeasurement(authenticationFacade.authentication.name, weightMeasurement)

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserInfo() = service.deleteUserInfoByAuthId(authenticationFacade.authentication.name)
}
