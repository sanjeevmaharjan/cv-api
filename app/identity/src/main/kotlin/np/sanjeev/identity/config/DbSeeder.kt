package np.sanjeev.identity.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManagerFactory
import jakarta.transaction.Transactional
import np.sanjeev.user.AppUser
import np.sanjeev.user.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DbSeeder(
    entityManagerFactory: EntityManagerFactory,
    private val encoder: PasswordEncoder,
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger { }
    private val em = { entityManagerFactory.createEntityManager() }

    @EventListener
    @Transactional
    fun seed(contextRefreshedEvent: ContextRefreshedEvent) {
        seedDefaultUser()
    }

    fun seedDefaultUser() {
        val check = em().createQuery("select t from AppUser t", AppUser::class.java).setMaxResults(1).resultList
        if (check.isNotEmpty()) {
            logger.info { "No Seeding Required" }
            return
        }

        val admin = User.withUsername("admin").password("123456").passwordEncoder(encoder::encode).build();

        val user = User.withUsername("sanjeev").password("123456").passwordEncoder(encoder::encode).build();

        userService.createUser(admin)
        userService.createUser(user)
    }
}
