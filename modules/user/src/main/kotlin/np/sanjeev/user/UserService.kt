package np.sanjeev.user

import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.provisioning.UserDetailsManager

interface UserService: UserDetailsManager, UserDetailsPasswordService
