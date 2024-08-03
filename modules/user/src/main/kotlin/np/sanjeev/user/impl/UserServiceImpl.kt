package np.sanjeev.user.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import np.sanjeev.user.AppUser
import np.sanjeev.user.UserRepository
import np.sanjeev.user.UserService
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {
    private val logger = KotlinLogging.logger {}
    private val usernameNotFoundException = UsernameNotFoundException("Username not found")

    var authenticationManager: AuthenticationManager? = null

    private val securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()

    private fun findByUsername(username: String?): AppUser? {
        logger.debug { "Searching user: $username in db." }
        return userRepository.findByUsername(username)
    }

    private fun toUserDetails(user: AppUser): UserDetails {
        return User.withUsername(user.username)
            .password(user.password)
            .roles("")
            .build()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = findByUsername(username) ?: throw usernameNotFoundException;
        return toUserDetails(user);
    }

    override fun createUser(user: UserDetails) {
        require(findByUsername(user.username) == null) { "username already exists" }

        val appUser = AppUser(null, user.username, user.password)

        logger.debug { "Saving user: $user" }
        userRepository.save(appUser)
    }

    override fun updateUser(user: UserDetails) {
        val appUser = findByUsername(user.username) ?: throw usernameNotFoundException

        logger.debug { "Updating user: $user" }
        userRepository.save(appUser)
    }

    override fun deleteUser(username: String?) {
        val appUser = findByUsername(username) ?: throw usernameNotFoundException

        logger.debug { "Deleting user: $username" }
        userRepository.delete(appUser)
    }

    override fun changePassword(oldPassword: String?, newPassword: String?) {
        val currentUser = securityContextHolderStrategy.context.authentication
            ?: throw AccessDeniedException("No Authentication found for changing the password")
        val username = currentUser.name

        // Re-authenticate for changing password
        logger.debug { "Will attempt re-authenticating the user if authentication Manager is set" }
        authenticationManager?.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(username, oldPassword)
        )

        val appUser = findByUsername(username) ?: throw usernameNotFoundException
        appUser.password = passwordEncoder.encode(newPassword)
        userRepository.save(appUser)
    }

    override fun userExists(username: String?): Boolean {
        return findByUsername(username) != null
    }

    override fun updatePassword(user: UserDetails, newPassword: String): UserDetails {
        val appUser = findByUsername(user.username) ?: throw usernameNotFoundException
        appUser.password = passwordEncoder.encode(newPassword)
        return toUserDetails(userRepository.save(appUser))
    }
}
