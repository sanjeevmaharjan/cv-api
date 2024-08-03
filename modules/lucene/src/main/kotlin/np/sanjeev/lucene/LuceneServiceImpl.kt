package np.sanjeev.lucene

import jakarta.persistence.EntityManagerFactory
import org.hibernate.search.mapper.orm.Search
import org.hibernate.search.mapper.orm.session.SearchSession
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class LuceneServiceImpl(entityManagerFactory: EntityManagerFactory) : LuceneService {
    private final val searchSession: SearchSession =
        Search.session(entityManagerFactory.createEntityManager())

    init {
        triggerIndexing()
    }

    final override fun triggerIndexing() = searchSession.massIndexer().threadsToLoadObjects(8)
        .startAndWait()

    @Bean
    final override fun getSearchSession(): SearchSession = searchSession
}
