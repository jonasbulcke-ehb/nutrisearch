package be.ehb.gdt.nutrisearch.domain.userinfo.services

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo

interface UserInfoService {
    fun getUserInfo(id: String): UserInfo
    fun getUserInfoByAuthId(authId: String): UserInfo
    fun hasUserInfoAuthId(authId: String): Boolean
    fun createUserInfo(authId: String, userInfo: UserInfo): UserInfo
    fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo)
    fun deleteUserInfoByAuthId(authId: String)
}