package be.ehb.gdt.nutrisearch.domain.userinfo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class MultipleUserInfoForAuthenticationException : RuntimeException() {
    override val message: String
        get() = "Authenticated user cannot have multiple user infos configured"

}
