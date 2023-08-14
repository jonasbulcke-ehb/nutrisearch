package be.ehb.gdt.nutrisearch.restapi.auth.services

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthenticationFacadeImpl : AuthenticationFacade {
    override val authentication: Authentication
        get() = SecurityContextHolder.getContext().authentication

    override val authId: String
        get() = authentication.name

    override fun isInRole(role: String): Boolean {
        val authority =
            if (role.contains("ROLE_")) SimpleGrantedAuthority(role) else SimpleGrantedAuthority("ROLE_$role")

        return authentication.authorities.contains(authority)
    }
}