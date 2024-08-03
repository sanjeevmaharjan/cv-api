package np.sanjeev.lucene

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer

class AppLuceneAnalysisConfigurer : LuceneAnalysisConfigurer {
    override fun configure(context: LuceneAnalysisConfigurationContext) {
        context.analyzer("english").custom()
            .tokenizer("standard")
            .tokenFilter("lowercase")
            .tokenFilter("snowballPorter").param("language", "English")
            .tokenFilter("asciiFolding")

        context.analyzer("name").custom()
            .tokenizer("standard")
            .tokenFilter("lowercase")
            .tokenFilter("asciiFolding")
    }
}
