package be.ehb.gdt.nutrisearch.config

import org.springframework.security.access.annotation.Secured

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Secured("ROLE_dietitian")
annotation class RequiresDietitianRole
