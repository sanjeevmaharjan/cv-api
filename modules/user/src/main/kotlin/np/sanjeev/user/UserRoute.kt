package np.sanjeev.user

import np.sanjeev.core.annotation.Route
import np.sanjeev.core.exception.ItemNotFoundException
import np.sanjeev.core.extension.pageable
import org.springframework.web.servlet.function.router

@Route
fun userCatalog(userCatalogService: UserCatalogService) = router {
    "/api/users".nest {
        GET("/{id}") { req ->
            val id = req.pathVariable("id").toBigIntegerOrNull()
            id?.let {
                userCatalogService.findUser(it)
                    ?.let { ok().body(it) }
            } ?: throw ItemNotFoundException(AppUser::class, id)
        }
        GET { req ->
            ok().body(userCatalogService.findUsers(req.pageable()))
        }
    }
}
