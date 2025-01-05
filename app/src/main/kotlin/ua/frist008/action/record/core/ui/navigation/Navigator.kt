package ua.frist008.action.record.core.ui.navigation

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.frist008.action.record.features.NavCommand

/**
 * Inject on [ua.frist008.action.record.core.ui.navigation.NavigationModule]
 */
@Stable class Navigator(
    sharedFlow: MutableSharedFlow<NavCommand> = MutableSharedFlow(),
) : MutableSharedFlow<NavCommand> by sharedFlow {

    @Deprecated(
        message = "Unsupported",
        replaceWith = ReplaceWith("emit(value)"),
        level = DeprecationLevel.ERROR,
    )
    override fun tryEmit(value: NavCommand): Nothing = throw UnsupportedOperationException()
}
