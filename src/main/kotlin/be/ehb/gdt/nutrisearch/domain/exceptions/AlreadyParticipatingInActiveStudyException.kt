package be.ehb.gdt.nutrisearch.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class AlreadyParticipatingInActiveStudyException(val userInfoId: String, val studyId: String) : RuntimeException() {
    override val message: String
        get() = "User with id $userInfoId is already participating in active study with id $studyId"

}
