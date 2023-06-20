package be.ehb.gdt.nutrisearch.domain.userinfo.services

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement

interface UserInfoService {
    fun getUserInfo(id: String): UserInfo
    fun getUserInfoByAuthId(authId: String): UserInfo
    fun hasUserInfoAuthId(authId: String): Boolean
    fun createUserInfo(authId: String, userInfo: UserInfo): UserInfo
    fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo): UserInfo
    fun addWeightMeasurement(authId: String, weightMeasurement: WeightMeasurement)
    fun deleteUserInfoByAuthId(authId: String)
}