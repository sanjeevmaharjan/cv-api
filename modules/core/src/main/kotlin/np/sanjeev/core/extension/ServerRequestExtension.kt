package np.sanjeev.core.extension

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.paramOrNull

fun ServerRequest.pageable(): Pageable {
    var page = paramOrNull("page")?.toIntOrNull()
    if (page == null || page < 0) page = 0

    var size = paramOrNull("size")?.toIntOrNull()
    if (size == null || size < 0) size = 12

    val sort = paramOrNull("sort")

    return if (sort != null)
        PageRequest.of(page, size, Sort.by(sort))
    else
        PageRequest.of(page, size)
}
