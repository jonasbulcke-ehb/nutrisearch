package be.ehb.gdt.nutrisearch.restapi.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
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
        http.csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint { _, response, _ ->
                response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"")
                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
            }
            .and()
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").authenticated()
            .requestMatchers("/api/v1/categories/**").hasRole("dietitian")
            .requestMatchers("/api/v1/products/{id}/verify").hasRole("dietitian")
            .requestMatchers(HttpMethod.GET, "/api/v1/products/{productId}/preparations/**").authenticated()
            .requestMatchers("/api/v1/products/{productId}/preparations/**").authenticated()
            .requestMatchers("/api/v1/userinfo/has-userinfo").authenticated()
            .requestMatchers("/api/v1/userinfo/weight").authenticated()
            .requestMatchers("/api/v1/userinfo/{id}").hasRole("dietitian")
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer().jwt()

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