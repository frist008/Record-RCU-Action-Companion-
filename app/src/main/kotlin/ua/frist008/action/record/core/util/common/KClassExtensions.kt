package ua.frist008.action.record.core.util.common

import kotlin.reflect.KClass

inline val KClass<*>.qualifiedNameOrThrow: String get() = requireNotNull(qualifiedName)
