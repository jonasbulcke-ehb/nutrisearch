package be.ehb.gdt.nutrisearch.domain.dietitians.services

import be.ehb.gdt.nutrisearch.domain.dietitians.entities.Dietitian
import be.ehb.gdt.nutrisearch.domain.dietitians.exceptions.DietitianRegistrationException
import be.ehb.gdt.nutrisearch.domain.dietitians.repositories.DietitianRepository
import be.ehb.gdt.nutrisearch.domain.dietitians.valueobjects.DietitianRegistrationRecord
import be.ehb.gdt.nutrisearch.restapi.auth.services.AuthenticationFacade
import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import org.springframework.stereotype.Service

@Service
class DietitianServiceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val repo: DietitianRepository,
    private val authFacade: AuthenticationFacade
) : DietitianService {
    override fun getDietitians() = repo.findDietitians()

    override fun registerDietitian(registrationRecord: DietitianRegistrationRecord) {
        if (repo.existsById(registrationRecord.riziv)) {
            throw DietitianRegistrationException(DietitianRegistrationException.ErrorCode.RIZIV_ALREADY_REGISTERED)
        }
        if(!isRizivValid(registrationRecord.riziv)) {
           throw DietitianRegistrationException(DietitianRegistrationException.ErrorCode.INVALID_RIZIV)
        }
        val claims = mapOf("roles" to listOf("dietitian"))
        var userRecord: UserRecord? = null
        try {
            val createRequest = UserRecord.CreateRequest()
                .setEmail(registrationRecord.email)
                .setDisplayName(registrationRecord.fullName)

            userRecord = firebaseAuth.createUser(createRequest).also {
                firebaseAuth.generatePasswordResetLinkAsync(it.email)
            }
        } catch (e: FirebaseAuthException) {
            if (e.authErrorCode == AuthErrorCode.EMAIL_ALREADY_EXISTS) {
                userRecord = firebaseAuth.getUserByEmail(registrationRecord.email)
                if(repo.existsByAuthId(userRecord!!.uid)) {
                    throw DietitianRegistrationException(DietitianRegistrationException.ErrorCode.EMAIL_ALREADY_USED)
                }
                userRecord.updateRequest().setDisplayName(registrationRecord.fullName).also {
                    firebaseAuth.updateUser(it)
                }
            }

        }

        if (userRecord != null) {
            firebaseAuth.setCustomUserClaims(userRecord.uid, claims)
            Dietitian(
                registrationRecord.riziv,
                registrationRecord.firstname,
                registrationRecord.lastname,
                userRecord.uid
            ).also { repo.insertDietitian(it) }
        }
    }

    override fun updateDietitian(registrationRecord: DietitianRegistrationRecord) {
        val updateRequest = UserRecord.UpdateRequest(authFacade.authId)
            .setDisplayName(registrationRecord.fullName)
        firebaseAuth.updateUser(updateRequest)
        repo.updateDietitian(registrationRecord.riziv, registrationRecord.firstname, registrationRecord.lastname)
    }

    override fun deleteDietitian(id: String) {
        firebaseAuth.deleteUser(authFacade.authId)
        repo.deleteDietitian(id)
    }

    private fun isRizivValid(riziv: String) : Boolean {
        if(riziv.length != 8) {
            return false
        }
        val digits = riziv.let {
            it.substring(0, 6).toIntOrNull() to it.substring(6).toIntOrNull()
        }
        if (digits.toList().contains(null)) {
            return false
        }
        for (divider in listOf(97, 89)) {
            val modulo = digits.first!! % divider
            if(divider - modulo == digits.second) {
                return true
            }
        }
        return false
    }
}