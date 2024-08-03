package np.sanjeev.cv

import np.sanjeev.core.annotation.Route
import np.sanjeev.core.exception.ItemNotFoundException
import np.sanjeev.core.extension.pageable
import np.sanjeev.core.extension.unwrap
import org.springframework.web.servlet.function.router

@Route
fun CvRoute(cvService: CvService) = router {
    "/api/cvs".nest {
        GET("/{id}") { req ->
            val id = req.pathVariable("id").toBigIntegerOrNull()
            id?.let {
                cvService.findCv(it)
                    ?.let { ok().body(it) }
            } ?: throw ItemNotFoundException(Cv::class, id)
        }
        GET { req ->
            var query = req.param("q").unwrap()
            ok().body(cvService.findCvs(query, req.pageable()))
        }
    }
}
