package be.ehb.gdt.nutrisearch.domain.userinfo.repositories

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo

interface UserInfoRepository {
    fun findUserInfoById(id: String): UserInfo?
    fun findUserInfoByAuthId(authId: String): UserInfo?
    fun insertUserInfo(userInfo: UserInfo): UserInfo
    fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo)
    fun existUserInfoById(id: String): Boolean
    fun existUserInfoByAuthId(authId: String): Boolean
}