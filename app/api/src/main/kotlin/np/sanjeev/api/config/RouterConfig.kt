package np.sanjeev.api.config


import io.github.oshai.kotlinlogging.KotlinLogging
import np.sanjeev.api.util.configureRoute
import np.sanjeev.core.annotation.Route
import np.sanjeev.core.util.getAllAnnotatedWith
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import kotlin.reflect.jvm.javaType

@Configuration
class RouterConfig {
    val logger = KotlinLogging.logger { }

    @Bean
    fun allRoutes(applicationContext: ApplicationContext): RouterFunction<ServerResponse> {
        val routes = getAllAnnotatedWith(Route::class)
        val all = routes.map { r ->
            val params = r.parameters.map { p ->
                applicationContext.getBean(Class.forName(p.type.javaType.typeName))
            }.toTypedArray()
            @Suppress("UNCHECKED_CAST")
            r.call(*params) as RouterFunction<ServerResponse>
        }

        logger.info { "Scan found ${routes.size} endpoints" }
        routes.forEach { r -> logger.debug { "Found: ${r.name}" } }
        return configureRoute(*all.toTypedArray())
    }
}
