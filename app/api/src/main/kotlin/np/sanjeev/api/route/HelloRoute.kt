package np.sanjeev.api.route

import np.sanjeev.core.annotation.Route
import org.springframework.web.servlet.function.router


@Route
fun greet() = router {
    GET("/hello") {
        ok().body("Hello World!")
    }
}
