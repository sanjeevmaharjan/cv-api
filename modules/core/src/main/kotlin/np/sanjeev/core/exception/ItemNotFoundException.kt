package np.sanjeev.core.exception

import np.sanjeev.core.BaseEntity
import kotlin.reflect.KClass

class ItemNotFoundException(val klass: KClass<out BaseEntity>, val id: Any?) :
    RuntimeException("Item of type `${klass.qualifiedName}` with id: `$id` not found")
