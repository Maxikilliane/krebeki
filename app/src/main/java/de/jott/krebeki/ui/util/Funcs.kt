package de.jott.krebeki.ui.util

import io.reactivex.functions.Predicate
import kotlin.reflect.KFunction

fun <T> not(value: KFunction<Boolean>): Predicate<T> = Predicate {
  @Suppress("NO_REFLECTION_IN_CLASS_PATH")
  !value.call(it)
}
