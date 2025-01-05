package ua.frist008.action.record.core.util.collection

import androidx.annotation.CheckResult
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@CheckResult fun <K, V> Sequence<Map.Entry<K, V>>.toMap(): Map<K, V> =
    map(Map.Entry<K, V>::toPair).toMap()

@CheckResult fun <E, R> Sequence<E?>.toNotNullImmutableList(block: (E) -> R?): ImmutableList<R> =
    this.filterNotNull().mapNotNull(block).toImmutableList()

@CheckResult
fun <E, R> Sequence<E>.toNotNullResultImmutableList(block: (E) -> R?): ImmutableList<R> =
    this.mapNotNull(block).toImmutableList()

@CheckResult fun <E, R> Sequence<E>.toImmutableList(block: (E) -> R): ImmutableList<R> =
    this.map(block).toImmutableList()

@CheckResult fun Sequence<String?>.mapNotNullOrEmpty(): Sequence<String> =
    this.mapNotNull { it?.ifEmpty { null } }
