package np.sanjeev.cv

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import np.sanjeev.core.BaseEntity
import org.hibernate.search.engine.backend.types.Projectable
import org.hibernate.search.engine.backend.types.TermVector.WITH_POSITIONS_OFFSETS_PAYLOADS
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import java.math.BigInteger

@Entity
@Indexed
class Cv(
    @Id @GeneratedValue var id: BigInteger?,

    @FullTextField(
        analyzer = "english",
        termVector = WITH_POSITIONS_OFFSETS_PAYLOADS,
        projectable = Projectable.YES
    )
    @Column(
        columnDefinition = "LONGTEXT"
    ) var resumeText: String,

    @Column(columnDefinition = "LONGTEXT") var resumeHtml: String,

    @FullTextField(
        analyzer = "english",
        termVector = WITH_POSITIONS_OFFSETS_PAYLOADS,
        projectable = Projectable.YES
    ) var category: String
) : BaseEntity()
