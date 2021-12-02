package util

import java.io.File
import java.lang.StackWalker.Option
import java.lang.StackWalker.StackFrame
import kotlin.reflect.typeOf
import kotlin.streams.asSequence

fun readInput(name: String) =
    File("src/${getDirectCaller().packageName.substringBefore('.')}", "$name.txt").readLines()

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

private fun getDirectCaller(): Class<*> =
    StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).walk { stream ->
        stream.asSequence()
            .map(StackFrame::getDeclaringClass)
            .drop(2)
            .first()
    }

