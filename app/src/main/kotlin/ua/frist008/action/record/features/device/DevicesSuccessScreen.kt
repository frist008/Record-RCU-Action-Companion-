package ua.frist008.action.record.features.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import ua.frist008.action.record.R
import ua.frist008.action.record.core.ui.component.image.CircleStatusComponent
import ua.frist008.action.record.core.ui.component.item.TextColumnItemComponent
import ua.frist008.action.record.core.ui.component.root.DefaultScaffold
import ua.frist008.action.record.core.ui.theme.AppTheme
import ua.frist008.action.record.features.device.entity.DeviceSuccessState
import ua.frist008.action.record.features.device.entity.DevicesSuccessState

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun DevicesSuccessScreen(
    @PreviewParameter(DevicesProvider::class) state: DevicesSuccessState,
    onItemClick: (DeviceSuccessState) -> Unit = {},
) {
    DefaultScaffold(
        title = stringResource(R.string.devices_title),
        backIcon = null,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 16.dp),
        ) {
            items(
                items = state.list,
                key = {
                    // TODO Replace to id from DB
                    it.address
                },
            ) { DeviceItem(it, onItemClick) }
        }
    }
}

@Composable
private fun DeviceItem(
    device: DeviceSuccessState,
    onItemClick: (DeviceSuccessState) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(1.dp)
            .background(AppTheme.colors.primaryContainer)
            .clickable { onItemClick(device) }
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            imageVector = device.deviceType.image,
            contentDescription = stringResource(id = device.deviceType.description),
            modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))

        TextColumnItemComponent(
            text = device.name,
            subtext = device.address,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
        )

        Spacer(modifier = Modifier.width(16.dp))
        CircleStatusComponent(device.isAvailableStatus)
    }
}

private class DevicesProvider : PreviewParameterProvider<DevicesSuccessState> {

    override val values = sequenceOf(
        DevicesSuccessState(
            persistentListOf(
                DeviceSuccessState(
                    id = 0,
                    isAvailableStatus = true,
                    name = LoremIpsum(6).values.first(),
                    address = "192.168.0.1:2555",
                ),
                DeviceSuccessState(
                    id = 0,
                    isAvailableStatus = true,
                    name = "Name PC",
                    address = "192.168.0.2:2555",
                ),
                DeviceSuccessState(
                    id = 0,
                    isAvailableStatus = false,
                    name = "Name PC",
                    address = "192.168.0.3:2555",
                ),
                DeviceSuccessState(
                    id = 0,
                    isAvailableStatus = false,
                    name = "Name PC",
                    address = "192.168.0.4:2555",
                ),
            ),
        ),
    )
}
