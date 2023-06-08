package be.ehb.gdt.nutrisearch.domain.userinfo.repositories

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement

interface UserInfoRepository {
    fun findUserInfoById(id: String): UserInfo?
    fun findUserInfoByAuthId(authId: String): UserInfo?
    fun findUserInfoIdByAuthId(authId: String): String?
    fun insertUserInfo(userInfo: UserInfo): UserInfo
    fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo)
    fun insertWeight(authId: String, weightMeasurement: WeightMeasurement)
    fun deleteUserInfoByAuthId(authId: String)
    fun hardDeleteUserInfoByAuthId(authId: String)
    fun existUserInfoById(id: String): Boolean
    fun existUserInfoByAuthId(authId: String): Boolean
}