package np.sanjeev.identity.config

import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import np.sanjeev.identity.AppRefreshTokenGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import org.springframework.util.ResourceUtils
import java.nio.file.Files

@Configuration
class AuthServerConfig {

    @Bean
    @Order(1)
    fun authServerSecurityFilterChain(
        http: HttpSecurity,
        tokenGenerator: OAuth2TokenGenerator<*>
    ): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
            .tokenGenerator(tokenGenerator)

        http {
            exceptionHandling {
                defaultAuthenticationEntryPointFor(
                    LoginUrlAuthenticationEntryPoint("/login"),
                    MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            }
            cors { Customizer.withDefaults<Any>() }
        }

        return http.build()
    }

    fun jwkSource(): JWKSource<SecurityContext> {
        val pemFile = ResourceUtils.getFile("classpath:keys/keypair.pem")
        val pemContent = String(Files.readAllBytes(pemFile.toPath()))
        val jwkES256 = ECKey.parseFromPEMEncodedObjects(pemContent)

        val jwkSet = JWKSet(jwkES256)
        return JWKSource { jwkSelector, _ -> jwkSelector.select(jwkSet) }
    }

    fun jwtEncoder() = NimbusJwtEncoder(jwkSource())

    @Bean
    fun jwtGenerator() = JwtGenerator(jwtEncoder())

    @Bean
    fun tokenGenerator(jwtGenerator: JwtGenerator): OAuth2TokenGenerator<*> {
        val authTokenGenerator = OAuth2AccessTokenGenerator()
        val refreshTokenGenerator = AppRefreshTokenGenerator()
        return DelegatingOAuth2TokenGenerator(jwtGenerator, authTokenGenerator, refreshTokenGenerator)
    }
}
