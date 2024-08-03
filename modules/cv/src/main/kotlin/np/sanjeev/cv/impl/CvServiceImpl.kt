package np.sanjeev.cv.impl

import np.sanjeev.cv.Cv
import np.sanjeev.cv.transport.CvProjectionDto
import np.sanjeev.cv.CvService
import np.sanjeev.lucene.extension.*
import org.hibernate.search.mapper.orm.session.SearchSession
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedModel
import org.springframework.stereotype.Service
import java.math.BigInteger

@Service
class CvServiceImpl(private val searchSession: SearchSession) : CvService {
    override fun findCvs(query: String?, pageable: Pageable): PagedModel<*> {
        val q = searchSession.search(Cv::class.java)

        return if (query == null) {
            q.where { f -> f.matchAll() }.getPage<Cv>(pageable)
        } else {
            val res = q
                .select(CvProjectionDto::class.java)
                .where { f -> f.match().fields("resumeText", "category").matching(query) }
                .getPage<CvProjectionDto>(pageable)

            res
        }
    }

    override fun findCv(id: BigInteger): Cv? =
        searchSession.search(Cv::class.java)
            .where { f -> f.id().matching(id) }
            .get()
}
