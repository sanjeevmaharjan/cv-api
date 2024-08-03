package np.sanjeev.user.impl

import np.sanjeev.core.extension.getPagedModel
import np.sanjeev.core.extension.unwrap
import np.sanjeev.user.AppUser
import np.sanjeev.user.UserCatalogService
import np.sanjeev.user.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class UserCatalogServiceImpl(private val userRepository: UserRepository) : UserCatalogService {

    override fun findUsers(pageable: Pageable): PagedModel<AppUser> = userRepository.findAll(pageable).getPagedModel()

    override fun findUser(id: BigInteger): AppUser? = userRepository.findById(id).unwrap()
}
