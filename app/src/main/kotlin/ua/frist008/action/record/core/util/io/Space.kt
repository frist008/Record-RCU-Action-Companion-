package ua.frist008.action.record.core.util.io

data class Space(val bytes: Long) {

    val kb: Double get() = bytes / KB_IN_BYTES.toDouble()
    val mb: Double get() = bytes / MB_IN_BYTES.toDouble()
    val gb: Double get() = bytes / GB_IN_BYTES.toDouble()

    operator fun compareTo(target: Space): Int = bytes.compareTo(target.bytes)

    fun isEmpty(): Boolean = bytes == 0L

    companion object {
        const val KB_IN_BYTES: Short = 1024
        const val MB_IN_BYTES: Int = KB_IN_BYTES * 1024
        const val GB_IN_BYTES: Long = MB_IN_BYTES * 1024L
    }
}

val Byte.bytes: Space get() = Space(this.toLong())

val Double.bytes: Space get() = Space(this.toLong())
val Double.kb: Space get() = Space((this * Space.KB_IN_BYTES).toLong())
val Double.mb: Space get() = Space((this * Space.MB_IN_BYTES).toLong())
val Double.gb: Space get() = Space((this * Space.GB_IN_BYTES).toLong())

val Long.bytes: Space get() = Space(this)
val Long.kb: Space get() = Space(this * Space.KB_IN_BYTES)
val Long.mb: Space get() = Space(this * Space.MB_IN_BYTES)
val Long.gb: Space get() = Space(this * Space.GB_IN_BYTES)

val Int.bytes: Space get() = Space(this.toLong())
val Int.kb: Space get() = Space(this.toLong() * Space.KB_IN_BYTES)
val Int.mb: Space get() = Space(this.toLong() * Space.MB_IN_BYTES)
val Int.gb: Space get() = Space(this * Space.GB_IN_BYTES)

val Float.bytes: Space get() = Space(this.toLong())
val Float.kb: Space get() = Space((this * Space.KB_IN_BYTES).toLong())
val Float.mb: Space get() = Space((this * Space.MB_IN_BYTES).toLong())
val Float.gb: Space get() = Space((this * Space.GB_IN_BYTES).toLong())
