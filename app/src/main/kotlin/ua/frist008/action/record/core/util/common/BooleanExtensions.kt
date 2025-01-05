package ua.frist008.action.record.core.util.common

import androidx.annotation.CheckResult
import androidx.annotation.IntRange

@CheckResult
@IntRange(from = 0, to = 1)
fun Boolean?.toInt(): Int = if (this == true) 1 else 0
