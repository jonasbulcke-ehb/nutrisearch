package be.ehb.gdt.nutrisearch.domain.userinfo.services

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import be.ehb.gdt.nutrisearch.domain.study.entities.Study
import be.ehb.gdt.nutrisearch.domain.userinfo.entities.UserInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.MultipleUserInfoForAuthenticationException
import be.ehb.gdt.nutrisearch.domain.userinfo.exceptions.NoUserInfoForAuthenticationFound
import be.ehb.gdt.nutrisearch.domain.userinfo.repositories.UserInfoRepository
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.UserUpdatableInfo
import be.ehb.gdt.nutrisearch.domain.userinfo.valueobjects.WeightMeasurement
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import org.springframework.stereotype.Service

@Service
class UserInfoServiceImpl(
    private val repo: UserInfoRepository,
    private val authFacade: AuthenticationFacade,
) : UserInfoService {

    override fun getAuthenticatedUserInfo() =
        repo.findUserInfoByAuthId(authFacade.authId) ?: throw NoUserInfoForAuthenticationFound()

    override fun getAuthenticationCurrentStudy(): Study? = repo.findCurrentStudyByAuthId(authFacade.authId)

    override fun getAuthenticationTreatmentTeam(): List<Dietitian> {
        if (!repo.existUserInfoByAuthId(authFacade.authId)) {
            throw NoUserInfoForAuthenticationFound()
        }
        return repo.findTreatmentTeamByAuthId(authFacade.authId)
    }

    override fun hasAuthenticationLinkedUserInfo() = repo.existUserInfoByAuthId(authFacade.authId)

    override fun createUserInfo(userInfo: UserInfo): UserInfo {
        if (repo.existUserInfoByAuthId(authFacade.authId)) {
            throw MultipleUserInfoForAuthenticationException()
        }
        userInfo.authId = authFacade.authId
        return repo.insertUserInfo(userInfo)
    }

    override fun updateUserInfo(userUpdatableInfo: UserUpdatableInfo): UserInfo {
        if (!repo.existUserInfoByAuthId(authFacade.authId)) {
            throw NoUserInfoForAuthenticationFound()
        }
        repo.updateUserInfo(authFacade.authId, userUpdatableInfo)
        return repo.findUserInfoByAuthId(authFacade.authId)!!
    }

    override fun addWeightMeasurement(weightMeasurement: WeightMeasurement) {
        if (!repo.existUserInfoByAuthId(authFacade.authId)) {
            throw NoUserInfoForAuthenticationFound()
        }
        repo.insertWeight(authFacade.authId, weightMeasurement)
    }

    override fun deleteUserInfoByAuthId() {
        repo.deleteUserInfoByAuthId(authFacade.authId)
    }

    override fun addToTreatmentTeam(riziv: String) {
        if (!repo.findTreatmentTeamByAuthId(authFacade.authId).any { it.riziv == riziv }) {
            repo.insertIdToTreatmentTeam(authFacade.authId, riziv)
        }
    }

    override fun deleteFromTreatmentTeam(riziv: String) = repo.deleteIdFromTreatmentTeam(authFacade.authId, riziv)
}