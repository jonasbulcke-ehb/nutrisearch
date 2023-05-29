package be.ehb.gdt.nutrisearch.restapi.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests()
            .anyRequest()
            .authenticated()

        http.oauth2ResourceServer().jwt()

        return http.build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val jwtAuthenticationConverter = JwtAuthenticationConverter()

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter { jwt ->
            jwt.getClaimAsStringList("roles")
                ?.map { "ROLE_$it" }
                ?.map { SimpleGrantedAuthority(it) }
        }

        return jwtAuthenticationConverter
    }


}