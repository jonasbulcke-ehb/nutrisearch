package be.ehb.gdt.nutrisearch.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseAppConfig {
    @Bean
    fun firebaseAuth(@Value("\${firebase.refresh-token-path}") refreshTokenPath: String): FirebaseAuth {
        FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream(refreshTokenPath)))
            .build().also {
                FirebaseApp.initializeApp(it)
                return FirebaseAuth.getInstance()
            }
    }
}