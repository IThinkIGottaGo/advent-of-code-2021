package util

import java.io.File
import java.lang.StackWalker.Option
import java.lang.StackWalker.StackFrame
import kotlin.reflect.typeOf
import kotlin.streams.asSequence

fun readInput(name: String) =
    File("src/${getDirectCaller().packageName.substringBefore('.')}", "$name.txt").readLines()

fun readInputAsInts(name: String) =
    File("src/${getDirectCaller().packageName.substringBefore('.')}", "$name.txt").readLines().map(String::toInt)

fun readInputOneLine(name: String) =
    File("src/${getDirectCaller().packageName.substringBefore('.')}", "$name.txt").readText()

fun String.splitBySpace() = this.split(" ")

inline fun <reified A, reified B> List<String>.stringsToPair(): Pair<A, B> {
    return Pair(stringToType(this[0].trim()), stringToType(this[1].trim()))
}

inline fun <reified T> stringToType(s: String) =
    when (typeOf<T>().classifier) {
        Int::class -> s.toInt() as T
        String::class -> s as T
        else -> error("unsupported type")
    }

inline fun <T, K> Iterable<T>.groupByIndexed(keySelector: (index: Int, T) -> K): Map<K, List<T>> {
    return groupByToIndexed(LinkedHashMap(), keySelector)
}

inline fun <T, K, M : MutableMap<in K, MutableList<T>>> Iterable<T>.groupByToIndexed(
    destination: M,
    keySelector: (index: Int, T) -> K
): M {
    for ((index, element) in this.withIndex()) {
        val key = keySelector(index, element)
        val list = destination.getOrPut(key) { ArrayList() }
        list.add(element)
    }
    return destination
}

inline fun <T> Sequence<T>.firstOrElse(predicate: (T) -> Boolean, defaultValue: T): T {
    for (element in this) if (predicate(element)) return element
    return defaultValue
}

private fun getDirectCaller(): Class<*> =
    StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).walk { stream ->
        stream.asSequence()
            .map(StackFrame::getDeclaringClass)
            .drop(2)
            .first()
    }

