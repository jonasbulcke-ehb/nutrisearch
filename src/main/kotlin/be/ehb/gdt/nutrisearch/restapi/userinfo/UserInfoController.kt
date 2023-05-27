package be.ehb.gdt.nutrisearch.restapi.userinfo

import be.ehb.gdt.nutrisearch.config.RequiresDietitianRole
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.services.UserInfoService
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/api/v1/userinfo")
class UserInfoController(private val service: UserInfoService) {
    @GetMapping
    fun getAuthorizedUserInfo(authentication: Authentication) = service.getUserInfoByAuthId(authentication.name)

    @GetMapping("/{id}")
    @RequiresDietitianRole
    fun getUserInfo(id: String) = service.getUserInfo(id)

    @GetMapping("/has-userinfo")
    fun hasAuthenticationLinkedUserInfo(authentication: Authentication) = service.hasUserInfoAuthId(authentication.name)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun postUserInfo(authentication: Authentication, userInfo: UserInfo) =
        service.createUserInfo(authentication.name, userInfo)

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun patchUserInfo(authentication: Authentication, userUpdatableInfo: UserUpdatableInfo) =
        service.updateUserInfo(authentication.name, userUpdatableInfo)


    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUserInfo(authentication: Authentication) = service.deleteUserInfoByAuthId(authentication.name)
}
