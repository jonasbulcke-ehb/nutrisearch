package be.ehb.gdt.nutrisearch.domain.userinfo.services

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import be.ehb.gdt.nutrisearch.domain.questionnaire.entities.Questionnaire
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import java.time.LocalDate

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
    fun addFavoriteProduct(productId: String)
    fun deleteFavoriteProduct(productId: String)
    fun getQuestionnaire(date: LocalDate): Questionnaire
    fun addAnswerToQuestionnaire(date: LocalDate, answerId: String, answer: String)
}