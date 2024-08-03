package np.sanjeev.identity.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes.*
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@Configuration
class AuthRegisteredClientConfig {
    private val WEB_URL = "http://localhost:3000"

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        config.addAllowedOrigin(WEB_URL)
        config.allowCredentials = true
        source.registerCorsConfiguration("/**", config)
        return source
    }


    @Bean
    fun registeredClientRepository(passwordEncoder: PasswordEncoder): RegisteredClientRepository = run {
        val reactClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("web-client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("$WEB_URL/auth/callback")
            .postLogoutRedirectUri(WEB_URL)
            .scope(OPENID)
            .scope(PROFILE)
            .scope(EMAIL)
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(true)
                    .requireProofKey(true).build()
            )
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.ES256)
                    .reuseRefreshTokens(false)
                    .build()
            )
            .build()

        val apiClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("api-client")
            .clientSecret(passwordEncoder.encode("123456"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:8080")
            .postLogoutRedirectUri("http://localhost:8080")
            .scope(OPENID)
            .scope(PROFILE)
            .scope(EMAIL)
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(true)
                    .requireProofKey(true).build()
            )
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.ES256)
                    .reuseRefreshTokens(false)
                    .build()
            )
            .build()

        InMemoryRegisteredClientRepository(reactClient, apiClient)
    }
}