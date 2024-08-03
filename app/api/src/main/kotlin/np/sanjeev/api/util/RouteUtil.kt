package np.sanjeev.api.util

import np.sanjeev.api.config.RouterConfig
import np.sanjeev.core.exception.ItemNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.ErrorResponse
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

fun RouterConfig.configureRoute(vararg routerFunction: RouterFunction<ServerResponse>) = router {
    routerFunction.forEach { x -> add(x) }
    before { serverRequest ->
        logger.debug { "Found a route which matches ${serverRequest.uri()}" }
        serverRequest
    }
    after { req, res ->
        when {
            res.statusCode().is2xxSuccessful -> {
                logger.debug { "Finished processing request ${req.uri()}" }
            }

            res.statusCode().is3xxRedirection -> {
                logger.debug { "Redirecting the request" }
            }

            res.statusCode().is4xxClientError || res.statusCode().is5xxServerError -> {
                logger.error { "Error processing request ${req.uri()}" }
            }
        }

        res
    }
    onError<ItemNotFoundException> { e, req ->
        logger.error { e }
        val res = ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.message!!)
            .type(req.uri())
            .build().body
        status(res.status).body(res)
    }
    onError<Throwable> { e, req ->
        logger.error { "Fatal exception occurred processing request $e" }
        val res = ErrorResponse.builder(e, HttpStatus.INTERNAL_SERVER_ERROR, e.message!!)
            .type(req.uri())
            .build().body
        status(res.status).body(res)
    }
}
