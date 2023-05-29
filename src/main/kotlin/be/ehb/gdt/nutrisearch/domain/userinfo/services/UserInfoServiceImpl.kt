package be.ehb.gdt.nutrisearch.domain.userinfo.services

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import org.springframework.stereotype.Service

@Service
class UserInfoServiceImpl(private val repo: UserInfoRepository) : UserInfoService {
    // TODO: create custom exceptions
    override fun getUserInfo(id: String) = repo.findUserInfoById(id) ?: throw RuntimeException()

    override fun getUserInfoByAuthId(authId: String) =
        repo.findUserInfoByAuthId(authId) ?: throw RuntimeException()

    override fun hasUserInfoAuthId(authId: String) = repo.existUserInfoByAuthId(authId)

    override fun createUserInfo(authId: String, userInfo: UserInfo): UserInfo {
        if(repo.existUserInfoByAuthId(authId)) {
            throw RuntimeException()
        }
        userInfo.authId = authId
        return repo.insertUserInfo(userInfo)
    }

    override fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo) {
        if (!repo.existUserInfoByAuthId(authId)) {
            throw RuntimeException()
        }
        repo.updateUserInfo(authId, userUpdatableInfo)
    }

    override fun addWeightMeasurement(authId: String, weightMeasurement: WeightMeasurement) {
        if(!repo.existUserInfoByAuthId(authId)) {
            throw RuntimeException()
        }
        repo.insertWeight(authId, weightMeasurement)
    }

    override fun deleteUserInfoByAuthId(authId: String) {
        val userInfo = repo.findUserInfoByAuthId(authId) ?: throw RuntimeException()
        userInfo.authId = null
        repo.insertUserInfo(userInfo)
    }
}