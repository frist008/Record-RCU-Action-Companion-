package ua.frist008.action.record.ui.entity.device

import androidx.compose.runtime.Stable
import ua.frist008.action.record.ui.entity.base.UIState

@Stable data class DeviceSuccessState(
    val status: Boolean,
    val type: DeviceType = if (status) DeviceType.ONLINE_PC else DeviceType.OFFLINE_PC,
    val name: String,
    val address: String,
) : UIState.Success()