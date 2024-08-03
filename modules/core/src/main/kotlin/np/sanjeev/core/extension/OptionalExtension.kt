package np.sanjeev.core.extension

import java.util.Optional

fun <T> Optional<T>.unwrap(): T? = orElse(null)
