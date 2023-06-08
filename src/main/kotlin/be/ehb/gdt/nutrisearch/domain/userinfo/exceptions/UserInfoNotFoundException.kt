package be.ehb.gdt.nutrisearch.domain.userinfo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserInfoNotFoundException(private val id: String) : RuntimeException() {
    override val message: String
        get() = "User info with id $id could not be found"
}
