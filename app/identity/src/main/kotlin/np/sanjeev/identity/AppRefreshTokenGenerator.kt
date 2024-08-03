package np.sanjeev.identity

import org.springframework.lang.Nullable
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator
import org.springframework.security.crypto.keygen.StringKeyGenerator
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import java.time.Instant
import java.util.*

class AppRefreshTokenGenerator : OAuth2TokenGenerator<OAuth2RefreshToken> {
    private val refreshTokenGenerator: StringKeyGenerator =
        Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96)

    @Nullable
    override fun generate(context: OAuth2TokenContext): OAuth2RefreshToken? {
        if (OAuth2TokenType.REFRESH_TOKEN != context.tokenType) {
            return null
        }

        if (isPublicClientForAuthorizationCodeGrant(context)) {
            return null
        }

        val issuedAt = Instant.now()
        val expiresAt = issuedAt.plus(context.registeredClient.tokenSettings.refreshTokenTimeToLive)
        return OAuth2RefreshToken(refreshTokenGenerator.generateKey(), issuedAt, expiresAt)
    }

    private fun isPublicClientForAuthorizationCodeGrant(context: OAuth2TokenContext): Boolean {
        if (AuthorizationGrantType.AUTHORIZATION_CODE == context.authorizationGrantType) {
            val principal = context.getAuthorizationGrant<Authentication>().principal
            if (principal is OAuth2ClientAuthenticationToken) {
                val isPublic = principal.clientAuthenticationMethod == ClientAuthenticationMethod.NONE

                val tokenSettings = principal.registeredClient?.tokenSettings ?: return isPublic

                return isPublic && !tokenSettings.isReuseRefreshTokens
            }
        }

        return false
    }
}
