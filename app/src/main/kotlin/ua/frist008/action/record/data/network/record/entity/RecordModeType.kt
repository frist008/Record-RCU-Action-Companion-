package ua.frist008.action.record.data.network.record.entity

enum class RecordModeType(val id: Int) {
    STOP(0),
    RECORD(1),
    PAUSE(2),
    ;

    companion object {

        operator fun get(index: Int): RecordModeType =
            entries.firstOrNull { it.id == index } ?: STOP
    }
}
