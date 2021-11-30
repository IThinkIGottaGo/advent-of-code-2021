package util

import java.io.File
import java.lang.StackWalker.Option
import java.lang.StackWalker.StackFrame
import kotlin.streams.asSequence

fun readInput(name: String) =
    File("src/${getDirectCaller().packageName}", "$name.txt").readLines()

private fun getDirectCaller(): Class<*> =
    StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).walk { stream ->
        stream.asSequence()
            .map(StackFrame::getDeclaringClass)
            .drop(2)
            .first()
    }

