package ua.frist008.action.record.features.record

import android.os.Vibrator
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import ua.frist008.action.record.R
import ua.frist008.action.record.analytics.Analytics
import ua.frist008.action.record.core.presentation.BaseViewModel
import ua.frist008.action.record.core.presentation.dependency.PresentationDependenciesDelegate
import ua.frist008.action.record.core.presentation.dependency.StateOwner.Companion.state
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.util.sound.VibratorEffect
import ua.frist008.action.record.core.util.sound.vibrate
import ua.frist008.action.record.data.network.DisconnectException
import ua.frist008.action.record.features.NavCommand
import ua.frist008.action.record.features.record.entity.FixedRecordCommand
import ua.frist008.action.record.features.record.entity.RecordSuccessState
import ua.frist008.action.record.features.record.entity.StreamingRecordCommand
import java.net.BindException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel class RecordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dependencies: PresentationDependenciesDelegate,
    private val recordRepository: RecordRepository,
    private val vibrator: Vibrator,
) : BaseViewModel(dependencies) {

    private val stateMutex = Mutex()

    val adListener = object : AdListener() {

        private var errorCounter = 0

        override fun onAdLoaded() {
            errorCounter = 0
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            if (++errorCounter > 10) {
                errorCounter = 0
                changeBannerSize()
            }
        }

        override fun onAdClosed() = changeBannerSize()
    }

    init {
        val pcId = savedStateHandle.toRoute<NavCommand.RecordScreen>().pcId

        launch {
            while (viewModelScope.isActive) {
                try {
                    recordRepository.connect(pcId)
                } catch (e: SocketTimeoutException) {
                    stateMutex.withLock { mutableState.emit(UIState.Progress()) }
                    Timber.d(e)
                }
            }
        }

        launch {
            recordRepository.recordFlow.collectLatest {
                stateMutex.withLock {
                    if (it.connected) {
                        val state = it.toUI(state.value as? RecordSuccessState?)
                        mutableState.emit(state)
                        Analytics.log(state)
                    } else {
                        mutableState.emit(UIState.Progress())
                    }
                }
            }
        }

        launch(Dispatchers.IO) {
            Analytics.subscribeAndBlock()
        }
    }

    fun onStartClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)

        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.START)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.START)
            }
        }
    }

    fun onPauseClick() {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_SLOW_RISE)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.PAUSE)
        }
    }

    fun onResumeClick() {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_QUICK_RISE)

        launch {
            recordRepository.sendCommand(FixedRecordCommand.RESUME)
        }
    }

    fun onStopClick(state: RecordSuccessState = state()) {
        vibrator.vibrate(VibratorEffect.PRIMITIVE_CLICK)

        launch {
            if (state.live.isLive) {
                recordRepository.sendCommand(FixedRecordCommand.STOP)
            } else {
                recordRepository.sendCommand(StreamingRecordCommand.STOP)
            }
        }
    }

    private fun changeBannerSize() {
        launch {
            stateMutex.withLock {
                val state = state.value as? RecordSuccessState? ?: return@launch
                val oldSize = state.adSize
                val newSize = if (oldSize == AdSize.BANNER) AdSize.LARGE_BANNER else AdSize.BANNER
                mutableState.emit(state.copy(adSize = newSize))
            }
        }
    }

    override suspend fun onFailure(cause: Throwable) {
        when (cause) {
            is DisconnectException -> navigator.emit(NavCommand.BackCommand())

            is ConnectException,
            is BindException,
                -> {
                toastFlow.emit(R.string.record_error_already_used)
                navigator.emit(NavCommand.BackCommand())
            }

            else -> {
                toastFlow.emit(R.string.error_unknown_error)
                navigator.emit(NavCommand.BackCommand())
                super.onFailure(cause)
            }
        }
    }
}
