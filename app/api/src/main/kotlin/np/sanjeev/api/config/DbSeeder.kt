package np.sanjeev.api.config

import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvToBeanBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManagerFactory
import jakarta.transaction.Transactional
import np.sanjeev.cv.Cv
import np.sanjeev.cv.CvRepository
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.util.ResourceUtils
import java.nio.file.Files

@Configuration
class DbSeeder(
    entityManagerFactory: EntityManagerFactory,
    private val cvRepository: CvRepository
) {
    private val logger = KotlinLogging.logger { }
    private val em = { entityManagerFactory.createEntityManager() }

    @EventListener
    @Transactional
    fun seed(contextRefreshedEvent: ContextRefreshedEvent) {
        seedCv()
    }


    fun seedCv() {
        val check = em().createQuery("select t from Cv t", Cv::class.java).setMaxResults(1).resultList
        if (check.isNotEmpty()) {
            logger.info { "No Seeding Required" }
            return
        }

        val file = ResourceUtils.getFile("classpath:dataset/cv.csv")

        val cvs = Files.newBufferedReader(file.toPath()).use {
            CsvToBeanBuilder<CvBean>(it).withType(CvBean::class.java).build().parse().map(CvBean::toCv)
        }

        cvRepository.saveAll(cvs)
    }
}

class CvBean {
    @CsvBindByName(column = "ID")
    var id: String = ""

    @CsvBindByName(column = "Resume_str")
    var resumeText: String = ""

    @CsvBindByName(column = "Resume_html")
    var resumeHtml: String = ""

    @CsvBindByName(column = "Category")
    var category: String = ""

    fun toCv() = Cv(id.toBigInteger(), resumeText, resumeHtml, category)
}
