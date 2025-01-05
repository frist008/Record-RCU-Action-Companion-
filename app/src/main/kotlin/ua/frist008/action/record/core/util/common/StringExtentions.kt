package ua.frist008.action.record.core.util.common

import androidx.annotation.CheckResult

@CheckResult fun String?.takeIfNotEmpty(): String? = this?.takeIf { it.isNotEmpty() }

@CheckResult fun String?.takeIfNotEmptyOrDefault(getDefault: () -> String): String =
    takeIfNotEmpty() ?: getDefault()

@CheckResult fun String?.takeIfEqual(target: String?): String? =
    this?.takeIf { it == target }

@CheckResult fun String?.takeIfEqual(target: () -> String?): String? =
    this?.takeIf { it == target() }

@CheckResult fun String?.takeIfNot(target: String?): String? = this?.takeIf { it != target }

@CheckResult fun String?.takeIfNot(target: () -> String?): String? =
    this?.takeIf { it != target() }
