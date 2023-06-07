package be.ehb.gdt.nutrisearch.domain.userinfo.services

import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.MultipleUserInfoForAuthenticationException
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.UserInfoNotFoundException
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import org.springframework.stereotype.Service

@Service
class UserInfoServiceImpl(private val repo: UserInfoRepository) : UserInfoService {
    override fun getUserInfo(id: String) = repo.findUserInfoById(id) ?: throw UserInfoNotFoundException(id)

    override fun getUserInfoByAuthId(authId: String) =
        repo.findUserInfoByAuthId(authId) ?: throw NoUserInfoForAuthenticationFound()

    override fun hasUserInfoAuthId(authId: String) = repo.existUserInfoByAuthId(authId)

    override fun createUserInfo(authId: String, userInfo: UserInfo): UserInfo {
        if(repo.existUserInfoByAuthId(authId)) {
            throw MultipleUserInfoForAuthenticationException()
        }
        userInfo.authId = authId
        return repo.insertUserInfo(userInfo)
    }

    override fun updateUserInfo(authId: String, userUpdatableInfo: UserUpdatableInfo) {
        if (!repo.existUserInfoByAuthId(authId)) {
            throw NoUserInfoForAuthenticationFound()
        }
        repo.updateUserInfo(authId, userUpdatableInfo)
    }

    override fun addWeightMeasurement(authId: String, weightMeasurement: WeightMeasurement) {
        if(!repo.existUserInfoByAuthId(authId)) {
            throw NoUserInfoForAuthenticationFound()
        }
        repo.insertWeight(authId, weightMeasurement)
    }

    override fun deleteUserInfoByAuthId(authId: String) {
        repo.deleteUserInfoByAuthId(authId)
    }
}