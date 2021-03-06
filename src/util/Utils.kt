package util

import java.io.File
import java.lang.StackWalker.Option
import java.lang.StackWalker.StackFrame
import kotlin.reflect.typeOf
import kotlin.streams.asSequence

fun readInput(name: String) =
    File("src/${getDirectCaller().packageName.substringBefore('.')}", "$name.txt")
        .readLines()

fun readInputAsInts(name: String) =
    File("src/${getDirectCaller().packageName.substringBefore('.')}", "$name.txt")
        .readLines()
        .map(String::toInt)

fun readInputOneLine(name: String) =
    File("src/${getDirectCaller().packageName.substringBefore('.')}", "$name.txt")
        .readText()

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
    destination: M, keySelector: (index: Int, T) -> K
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

inline fun <T> Sequence<T>.partitionWithIndex(predicate: (index: Int, T) -> Boolean): Pair<List<T>, List<T>> {
    val first = ArrayList<T>()
    val second = ArrayList<T>()
    for ((index, element) in this.withIndex()) {
        if (predicate(index, element)) {
            first.add(element)
        } else {
            second.add(element)
        }
    }
    return Pair(first, second)
}

inline fun <T> MutableList<T>.removeFirstIf(predicate: (T) -> Boolean): T {
    if (isEmpty()) throw NoSuchElementException("List is empty.")
    else {
        for ((index, element) in this.withIndex()) {
            if (predicate(element)) {
                return removeAt(index)
            }
        }
        throw NoSuchElementException("Collection contains no element matching the predicate.")
    }
}

inline fun <T> MutableList<T>.removeFromLastUntil(predicate: (T) -> Boolean): Boolean {
    var result = false
    val iterator = listIterator(size)
    while (iterator.hasPrevious()) {
        val e = iterator.previous()
        if (predicate(e)) {
            break
        } else {
            iterator.remove()
            result = true
        }
    }
    return result
}

private fun getDirectCaller(): Class<*> =
    StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
        .walk { stream ->
            stream.asSequence()
                .map(StackFrame::getDeclaringClass)
                .drop(2)
                .first()
        }