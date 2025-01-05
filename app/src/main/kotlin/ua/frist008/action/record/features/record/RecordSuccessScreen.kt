@file:Suppress("UnusedReceiverParameter")

package ua.frist008.action.record.features.record

import android.graphics.Color
import android.view.View
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import ua.frist008.action.record.BuildConfig
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.root.DefaultScaffold
import ua.frist008.action.record.core.ui.resource.svg.Pause
import ua.frist008.action.record.core.ui.resource.svg.Play
import ua.frist008.action.record.core.ui.resource.svg.PlayBold
import ua.frist008.action.record.core.ui.resource.svg.Stop
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.core.ui.theme.color.PreviewPalette
import ua.frist008.action.record.features.record.entity.EngineState
import ua.frist008.action.record.features.record.entity.ErrorType
import ua.frist008.action.record.features.record.entity.LiveState
import ua.frist008.action.record.features.record.entity.RecordButtonsState
import ua.frist008.action.record.features.record.entity.RecordSuccessState
import ua.frist008.action.record.features.record.entity.StorageState

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = PreviewPalette.PURPLE_LIGHT_LONG,
)
@Composable
fun RecordSuccessScreen(
    @PreviewParameter(RecordProvider::class) state: RecordSuccessState,
    adListener: AdListener = object : AdListener() {},
    onStartClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit = {},
) {
    DefaultScaffold(
        title = stringResource(
            id = if (state.live.isLive) {
                R.string.record_title_stream
            } else {
                R.string.record_title_record
            },
        ),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderInfo(engine = state.engine, live = state.live, storage = state.storage)
            FpsContainer(
                fpsState = state.fpsState,
                isPaused = state.buttonsData.isPaused,
                isRecording = state.buttonsData.isRecording,
            )
            TimeComponent(timeState = state.timeState)
            FooterActions(
                buttonsState = state.buttonsData,
                onStartClick = onStartClick,
                onResumeClick = onResumeClick,
                onPauseClick = onPauseClick,
                onStopClick = onStopClick,
            )
            Ads(adListener, state.adSize)
        }
    }
}

@Composable
private fun ColumnScope.HeaderInfo(engine: EngineState, live: LiveState, storage: StorageState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        EngineComponent(engine)
        LiveComponent(live)
        StorageComponent(storage)
    }
}

@Composable
private fun RowScope.EngineComponent(engine: EngineState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.record_header_engine),
            style = AppTheme.typography.bodyLarge,
            modifier = Modifier.padding(4.dp),
        )
        Text(
            text = engine.name,
            style = AppTheme.typography.titleLarge,
            modifier = Modifier.padding(4.dp),
            color = engine.errorType.getColor(AppTheme.colors),
        )
    }
}

@Composable
private fun RowScope.LiveComponent(live: LiveState) {
    if (live.isLive) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.record_header_live),
                style = AppTheme.typography.bodyLarge,
                modifier = Modifier.padding(4.dp),
            )
            Text(
                text = stringResource(
                    if (live.isOnline) {
                        R.string.record_header_live_on
                    } else {
                        R.string.record_header_live_off
                    },
                ),
                style = AppTheme.typography.titleLarge,
                color = if (live.isOnline) {
                    AppTheme.colors.record
                } else {
                    AppTheme.colors.stop
                },
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

@Composable
private fun RowScope.StorageComponent(storage: StorageState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StorageTitleText(PaddingValues(4.dp))
        StorageValueText(
            paddingValues = PaddingValues(4.dp),
            sizeState = storage.freeSpaceState,
            pattern = storage.pattern,
            errorType = storage.errorType,
        )
    }
}

@Composable
fun StorageTitleText(paddingValues: PaddingValues) {
    Text(
        text = stringResource(R.string.record_header_storage),
        style = AppTheme.typography.bodyLarge,
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
fun StorageValueText(
    paddingValues: PaddingValues,
    sizeState: State<Float>,
    @StringRes pattern: Int,
    errorType: ErrorType,
) {
    val size by remember { sizeState }

    Text(
        text = stringResource(pattern, size),
        style = AppTheme.typography.titleLarge,
        color = errorType.getColor(AppTheme.colors),
        modifier = Modifier.padding(paddingValues),
    )
}

@Composable
private fun ColumnScope.FpsContainer(
    fpsState: State<String>,
    isPaused: Boolean,
    isRecording: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        // TODO add Chart
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val fps by remember { fpsState }

            Text(
                text = fps,
                style = AppTheme.typography.displayLarge,
                color = when {
                    isPaused -> AppTheme.colors.pause
                    isRecording -> AppTheme.colors.record
                    else -> AppTheme.colors.fps
                },
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = (if (fps.length == 1) 21 else 0).dp),
            )
            FpsSuffix(isPaused, isRecording)
        }
    }
}

@Composable
private fun RowScope.FpsSuffix(isPaused: Boolean, isRecording: Boolean) {
    Text(
        text = stringResource(R.string.record_fps),
        style = AppTheme.typography.displaySmall,
        color = when {
            isPaused -> AppTheme.colors.pause
            isRecording -> AppTheme.colors.record
            else -> AppTheme.colors.fps
        },
        modifier = Modifier
            .padding(start = 8.dp, end = 64.dp)
            .padding(vertical = 12.dp)
            .align(Alignment.Bottom),
    )
}

@Composable
private fun ColumnScope.TimeComponent(timeState: MutableState<String>) {
    val time by remember { timeState }

    Text(
        text = time,
        style = AppTheme.typography.displaySmall,
        color = AppTheme.colors.onBackground,
        modifier = Modifier.padding(22.dp),
    )
}

@Composable
private fun ColumnScope.FooterActions(
    buttonsState: RecordButtonsState,
    onStartClick: () -> Unit,
    onResumeClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit,
) {
    Row(modifier = Modifier.padding(20.dp)) {
        val circleModifier = Modifier.clip(CircleShape)
        val isRecordingVisible = buttonsState.isRecordingVisible

        AnimatedVisibility(isRecordingVisible) {
            if (buttonsState.isRecording) {
                Icon(
                    imageVector = Icons.Pause,
                    contentDescription = stringResource(R.string.record_action_pause),
                    modifier = circleModifier.clickable(onClick = onPauseClick),
                )
            } else {
                Icon(
                    imageVector = if (buttonsState.isStopVisible) Icons.Play else Icons.PlayBold,
                    contentDescription = stringResource(R.string.record_action_record),
                    modifier = circleModifier.clickable(
                        onClick = if (buttonsState.isStopVisible) {
                            onResumeClick
                        } else {
                            onStartClick
                        },
                    ),
                )
            }
        }

        AnimatedVisibility(buttonsState.isStopVisible) {
            val widthStop = (if (isRecordingVisible) 50 else 0).dp + Icons.Stop.defaultWidth
            Row(modifier = Modifier.width(widthStop), horizontalArrangement = Arrangement.End) {
                Icon(
                    imageVector = Icons.Stop,
                    contentDescription = stringResource(R.string.record_action_stop),
                    modifier = circleModifier.clickable(onClick = onStopClick),
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.Ads(adListener: AdListener, adSize: AdSize) {
    val isInEditMode = BuildConfig.DEBUG && LocalView.current.isInEditMode
    // https://developers.google.com/admob/android/banner/fixed-size

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (adSize == AdSize.BANNER) 50.dp else 100.dp),
        factory = { context ->
            if (isInEditMode) {
                View(context).apply { setBackgroundColor(Color.WHITE) }
            } else {
                AdView(context).apply {
                    setAdSize(adSize)
                    adUnitId = context.getString(R.string.ad)
                    this.adListener = adListener
                    loadAd(
                        AdRequest.Builder()
                            .addKeyword("taxi")
                            .addKeyword("twitch")
                            .addKeyword("youtube")
                            .addKeyword("instagram")
                            .addKeyword("facebook")
                            .addKeyword("remote")
                            .addKeyword("game")
                            .addKeyword("games")
                            .addKeyword("gamer")
                            .addKeyword("gameplay")
                            .addKeyword("steam")
                            .addKeyword("stream")
                            .addKeyword("streaming")
                            .addKeyword("windows")
                            .addKeyword("controller")
                            .addKeyword("sony")
                            .addKeyword("playstation")
                            .addKeyword("nvidia")
                            .addKeyword("radeon")
                            .addKeyword("amd")
                            .addKeyword("intel")
                            .addKeyword("msi")
                            .addKeyword("samsung")
                            .addKeyword("iphone")
                            .addKeyword("android")
                            .addKeyword("asus")
                            .addKeyword("xbox")
                            .addKeyword("pc")
                            .addKeyword("phone")
                            .addKeyword("power bank")
                            .build(),
                    )
                }
            }
        },
    )
}

private class RecordProvider : PreviewParameterProvider<RecordSuccessState> {

    override val values = sequenceOf(
        RecordSuccessState.previewStop(),
        RecordSuccessState.previewRecording(),
        RecordSuccessState.previewPaused(),
        RecordSuccessState.previewLive(),
    )
}
