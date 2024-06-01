package ua.frist008.action.record.presentation.impl

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import ua.frist008.action.record.presentation.base.BaseViewModel
import ua.frist008.action.record.presentation.base.entity.PresentationDependenciesDelegate
import ua.frist008.action.record.ui.entity.SettingsUIEntity
import ua.frist008.action.record.ui.entity.base.UIState
import javax.inject.Inject

@HiltViewModel class SettingsViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel<UIState<SettingsUIEntity, Exception>>(dependencies) {

    override val mutableState = MutableStateFlow(UIState<SettingsUIEntity, Exception>())

    override fun onFailure(cause: Throwable) {
        Timber.e(cause)
    }
}
