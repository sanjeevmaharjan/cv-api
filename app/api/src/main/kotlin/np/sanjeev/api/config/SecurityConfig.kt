package np.sanjeev.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {
    private val WEB_URL = "http://localhost:3000"

    @Bean
    fun securityFilterChain(http: HttpSecurity, appIntrospector: OpaqueTokenIntrospector): SecurityFilterChain {
        http.formLogin { x -> x.disable() }
        http {
            authorizeHttpRequests {
                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer {
                opaqueToken {
                    introspector = appIntrospector
                }
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            cors { Customizer.withDefaults<Any>() }
        }

        return http.build()
    }

    @Bean
    fun introspector(): OpaqueTokenIntrospector =
        NimbusOpaqueTokenIntrospector("http://localhost:8080/oauth2/introspect", "api-client", "123456")

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

}
