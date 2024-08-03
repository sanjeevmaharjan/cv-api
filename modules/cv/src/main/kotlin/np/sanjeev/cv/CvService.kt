package np.sanjeev.cv

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedModel
import java.math.BigInteger

interface CvService {
    fun findCvs(query: String?, pageable: Pageable): PagedModel<*>
    fun findCv(id: BigInteger): Cv?
}
