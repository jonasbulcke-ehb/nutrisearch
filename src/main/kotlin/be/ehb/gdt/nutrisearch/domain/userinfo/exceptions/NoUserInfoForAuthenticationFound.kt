package be.ehb.gdt.nutrisearch.domain.userinfo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class NoUserInfoForAuthenticationFound: RuntimeException() {
    override val message: String
        get() = "No user info for authenticated user could be found"
}