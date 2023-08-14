package be.ehb.gdt.nutrisearch.domain.userinfo.services

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement

interface UserInfoService {
    fun getAuthenticatedUserInfo(): UserInfo
    fun getAuthenticationCurrentStudy(): Study?
    fun hasAuthenticationLinkedUserInfo(): Boolean
    fun createUserInfo(userInfo: UserInfo): UserInfo
    fun updateUserInfo(userUpdatableInfo: UserUpdatableInfo): UserInfo
    fun addWeightMeasurement(weightMeasurement: WeightMeasurement)
    fun deleteUserInfoByAuthId()
    fun getAuthenticationTreatmentTeam(): List<Dietitian>
    fun addToTreatmentTeam(riziv: String)
    fun deleteFromTreatmentTeam(riziv: String)
}