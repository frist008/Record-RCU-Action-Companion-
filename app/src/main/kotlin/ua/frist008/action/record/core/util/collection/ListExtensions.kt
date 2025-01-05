package ua.frist008.action.record.core.util.collection

import androidx.annotation.CheckResult
import kotlinx.collections.immutable.ImmutableList

@CheckResult fun <E, R> List<E?>.toNotNullImmutableList(block: (E) -> R?): ImmutableList<R> =
    this.asSequence().toNotNullImmutableList(block)

@CheckResult fun <E, R> List<E>.toNotNullResultImmutableList(block: (E) -> R?): ImmutableList<R> =
    this.asSequence().toNotNullResultImmutableList(block)

@CheckResult fun <E, R> List<E>.toImmutableList(block: (E) -> R): ImmutableList<R> =
    this.asSequence().toImmutableList(block)
