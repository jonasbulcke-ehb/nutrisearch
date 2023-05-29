package be.ehb.gdt.nutrisearch.restapi.auth.config

import org.springframework.security.access.annotation.Secured

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Secured("ROLE_dietitian")
annotation class RequiresDietitianRole
