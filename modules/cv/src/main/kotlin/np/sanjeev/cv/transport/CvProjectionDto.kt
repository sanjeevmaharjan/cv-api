package np.sanjeev.cv.transport

import np.sanjeev.cv.Cv
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*
import java.math.BigInteger

@ProjectionConstructor
data class CvProjectionDto(
    @EntityProjection
    val entity: Cv,

    @ScoreProjection
    val score: Float,

    @HighlightProjection
    val resumeText: List<String>,

    @HighlightProjection
    val category: List<String>,

    @IdProjection
    val id: BigInteger,
)
