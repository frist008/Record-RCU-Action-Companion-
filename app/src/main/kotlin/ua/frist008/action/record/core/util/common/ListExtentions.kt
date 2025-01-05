package ua.frist008.action.record.core.util.common

import androidx.annotation.CheckResult

@CheckResult public fun <T, L : Collection<T>> L?.takeIfNotEmpty(): L? =
    this?.takeIf { it.isNotEmpty() }

@CheckResult public fun <T, L : Collection<T>> L?.takeIfNotEmptyOrDefault(getDefault: () -> L): L =
    takeIfNotEmpty() ?: getDefault()
