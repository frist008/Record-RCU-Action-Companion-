package ua.frist008.action.record.data.repository

import kotlinx.coroutines.flow.Flow
import ua.frist008.action.record.domain.entity.DeviceDomainEntity

interface DeviceRadarRepository {

    fun get(): Flow<List<DeviceDomainEntity>>
}