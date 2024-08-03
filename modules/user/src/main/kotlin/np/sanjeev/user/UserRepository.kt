package np.sanjeev.user

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.CrudRepository
import java.math.BigInteger

interface UserRepository : CrudRepository<AppUser, BigInteger>, PagingAndSortingRepository<AppUser, BigInteger> {
    fun findByUsername(id: String?): AppUser?
}
