package np.sanjeev.user

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AuthConfig {
    private final val defaultEncoder = "argon2"

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encoders = mapOf(
            "bcrypt" to BCryptPasswordEncoder(),
            "argon2" to Argon2PasswordEncoder(16, 32, 1, 1 shl 14, 2)
        )
        return DelegatingPasswordEncoder(defaultEncoder, encoders);
    }
}
