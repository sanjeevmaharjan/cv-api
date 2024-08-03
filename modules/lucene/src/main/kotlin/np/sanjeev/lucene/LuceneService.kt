package np.sanjeev.lucene

import org.hibernate.search.mapper.orm.session.SearchSession

interface LuceneService {
    fun triggerIndexing()
    fun getSearchSession(): SearchSession
}