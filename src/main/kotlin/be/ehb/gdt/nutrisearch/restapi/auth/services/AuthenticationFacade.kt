package be.ehb.gdt.nutrisearch.restapi.auth.services

import org.springframework.security.core.Authentication

interface AuthenticationFacade {
    val authentication: Authentication

    fun isInRole(role: String): Boolean
}