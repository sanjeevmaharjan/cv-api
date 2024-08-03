package np.sanjeev.user

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedModel
import java.math.BigInteger

interface UserCatalogService {
    fun findUsers(pageable: Pageable): PagedModel<AppUser>
    fun findUser(id: BigInteger): AppUser?
}
