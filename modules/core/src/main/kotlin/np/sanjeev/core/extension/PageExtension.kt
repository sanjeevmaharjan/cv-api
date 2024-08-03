package np.sanjeev.core.extension

import org.springframework.data.domain.Page
import org.springframework.data.web.PagedModel

fun <T> Page<T>.getPagedModel(): PagedModel<T> {
    return PagedModel<T>(this)
}
