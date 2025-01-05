package ua.frist008.action.record.features.record.entity

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.google.android.gms.ads.AdSize
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.UIState
import ua.frist008.action.record.core.ui.theme.color.AppColorScheme

@Immutable data class RecordSuccessState(
    val fps: Int,
    val fpsState: MutableState<String> = mutableStateOf(fps.toString()),
    val maxFps: Int,
    val timeState: MutableState<String>,
    val engine: EngineState,
    val live: LiveState,
    val storage: StorageState,
    val buttonsData: RecordButtonsState,
    val isWebCam: Boolean,
    val isMic: Boolean,
    val gameActive: Boolean,
    val adSize: AdSize = AdSize.BANNER,
) : UIState.Success() {

    init {
        fpsState.value = fps.toString()
    }

    companion object {

        fun previewStop() = RecordSuccessState(
            fps = 60,
            maxFps = 100,
            timeState = mutableStateOf("00:00:00"),
            engine = EngineState.previewStopDefault(),
            live = LiveState.previewLiveOff(),
            storage = StorageState.previewDefault(),
            buttonsData = RecordButtonsState.previewStop(),
            gameActive = true,
            isWebCam = false,
            isMic = false,
        )

        fun previewRecording() = previewStop().copy(
            fps = 100,
            maxFps = 60,
            engine = EngineState.previewStopWarning(),
            live = LiveState.previewRecord(),
            storage = StorageState.previewWarning(),
            buttonsData = RecordButtonsState.previewRecording(),
            isWebCam = true,
            isMic = false,
        )

        fun previewPaused() = previewStop().copy(
            fps = 6,
            maxFps = 30,
            engine = EngineState.previewStopError(),
            live = LiveState.previewRecord(),
            storage = StorageState.previewError(),
            buttonsData = RecordButtonsState.previewPaused(),
            isWebCam = false,
            isMic = true,
        )

        fun previewLive() = previewStop().copy(
            maxFps = 30,
            engine = EngineState.previewStopDefault(),
            live = LiveState.previewLiveOn(),
            storage = StorageState.previewDefault(),
            buttonsData = RecordButtonsState.previewStream(),
            isWebCam = true,
            isMic = true,
        )
    }
}

@Immutable data class EngineState(
    val name: String,
    val errorType: ErrorType,
) {

    companion object {
        fun previewStopDefault() = EngineState(
            name = "Windows",
            errorType = ErrorType.ERROR,
        )

        fun previewStopWarning() = EngineState(
            name = "Unknown",
            errorType = ErrorType.WARNING,
        )

        fun previewStopError() = EngineState(
            name = "Vulkan",
            errorType = ErrorType.DEFAULT,
        )
    }
}

@Immutable data class LiveState(
    val isOnline: Boolean,
    val isLive: Boolean,
) {

    companion object {
        fun previewRecord() = LiveState(isOnline = false, isLive = false)
        fun previewLiveOff() = LiveState(isOnline = false, isLive = true)
        fun previewLiveOn() = LiveState(isOnline = true, isLive = true)
    }
}

@Immutable data class StorageState(
    val freeSpaceState: MutableState<Float>,
    @StringRes val pattern: Int,
    val errorType: ErrorType,
) {

    companion object {
        fun previewDefault() = StorageState(
            freeSpaceState = mutableFloatStateOf(30.4f),
            pattern = R.string.record_header_storage_gb_pattern,
            errorType = ErrorType.DEFAULT,
        )

        fun previewWarning() = StorageState(
            freeSpaceState = mutableFloatStateOf(9.72f),
            pattern = R.string.record_header_storage_gb_pattern,
            errorType = ErrorType.WARNING,
        )

        fun previewError() = StorageState(
            freeSpaceState = mutableFloatStateOf(30f),
            pattern = R.string.record_header_storage_mb_pattern,
            errorType = ErrorType.ERROR,
        )
    }
}

@Immutable data class RecordButtonsState(
    val isRecordingVisible: Boolean,
    val isStopVisible: Boolean,
    val isRecording: Boolean,
) {

    val isPaused = !isRecording && isStopVisible

    companion object {

        fun previewStop() = RecordButtonsState(
            isRecordingVisible = true,
            isRecording = false,
            isStopVisible = false,
        )

        fun previewRecording() = RecordButtonsState(
            isRecordingVisible = true,
            isRecording = true,
            isStopVisible = true,
        )

        fun previewPaused() = RecordButtonsState(
            isRecordingVisible = true,
            isRecording = false,
            isStopVisible = true,
        )

        fun previewStream() = RecordButtonsState(
            isRecordingVisible = false,
            isRecording = true,
            isStopVisible = true,
        )
    }
}

enum class ErrorType(val getColor: (AppColorScheme) -> Color) {
    DEFAULT({ it.onBackground }),
    WARNING({ it.warning }),
    ERROR({ it.error }),
}
