package be.ehb.gdt.nutrisearch.domain.userinfo.repositories

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement

interface UserInfoRepository {
    fun findUserInfoById(id: String): UserInfo?
    fun findUserInfoByAuthId(authId: String): UserInfo?
    fun findUserInfoIdByAuthId(authId: String): String?
    fun findCurrentStudyById(id: String): Study?
    fun findCurrentStudyByAuthId(authId: String): Study?
    fun findTreatmentTeamByAuthId(authId: String): List<Dietitian>
    fun findPatientsById(id: String): List<UserInfo>
    fun insertUserInfo(userInfo: UserInfo): UserInfo
    fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo)
    fun insertIdToTreatmentTeam(authId: String, id: String)
    fun insertWeight(authId: String, weightMeasurement: WeightMeasurement)
    fun deleteUserInfoByAuthId(authId: String)
    fun hardDeleteUserInfoByAuthId(authId: String)
    fun existUserInfoById(id: String): Boolean
    fun existUserInfoByAuthId(authId: String): Boolean
    fun deleteIdFromTreatmentTeam(authId: String, id: String)
}