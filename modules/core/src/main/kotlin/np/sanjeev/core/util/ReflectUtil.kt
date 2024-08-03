package np.sanjeev.core.util

import io.github.classgraph.ClassGraph
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.kotlinFunction

fun getAllAnnotatedWith(annotation: KClass<out Annotation>): List<KFunction<*>> {
    val `package` = "np.sanjeev"
    val annotationName = annotation.java.canonicalName

    return ClassGraph()
        .enableAllInfo()
        .acceptPackages(`package`)
        .scan().use { scanResult ->
            scanResult.getClassesWithMethodAnnotation(annotationName).flatMap { routeClassInfo ->
                routeClassInfo.methodInfo.filter { function ->
                    function.hasAnnotation(annotation.java)
                }.mapNotNull { method ->
                    method.loadClassAndGetMethod().kotlinFunction
                    // if parameter needed:
                    // method.getAnnotationInfo(routeAnnotation).parameterValues.map { it.value }
                }
            }
        }
}
