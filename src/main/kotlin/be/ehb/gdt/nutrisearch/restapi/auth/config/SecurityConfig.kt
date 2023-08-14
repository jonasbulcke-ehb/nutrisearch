package be.ehb.gdt.nutrisearch.restapi.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**").allowedOrigins("*").exposedHeaders(HttpHeaders.CONTENT_DISPOSITION)
            }
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"")
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
                }
            }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/api/v1/categories/**").authenticated()
                    .requestMatchers("/api/v1/categories/**").hasRole("dietitian")
                    .requestMatchers("/api/v1/products/{id}/verify").hasRole("dietitian")
                    .requestMatchers(HttpMethod.GET, "/api/v1/products/{productId}/preparations/**").authenticated()
                    .requestMatchers("/api/v1/products/{productId}/preparations/**").authenticated()
                    .requestMatchers("/api/v1/userinfo/**").authenticated()
                    .requestMatchers("/api/v1/userinfo/{id}").hasRole("dietitian")
                    .requestMatchers("/api/v1/consumptions/export-to-excel").permitAll()
                    .requestMatchers("/api/v1/consumptions/**").authenticated()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .build()
    }

    @Bean
    fun jwtAuthenticationConverter() = JwtAuthenticationConverter().apply {
        setJwtGrantedAuthoritiesConverter { jwt ->
            jwt.getClaimAsStringList("roles")
                ?.map { "ROLE_$it" }
                ?.map { SimpleGrantedAuthority(it) }
        }
    }

}