package ua.frist008.action.record.features.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import javax.inject.Inject

@HiltViewModel class SettingsViewModel @Inject constructor(
    dependencies: PresentationDependenciesDelegate,
) : BaseViewModel(dependencies)
