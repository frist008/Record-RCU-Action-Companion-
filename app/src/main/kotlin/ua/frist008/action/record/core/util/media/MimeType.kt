package ua.frist008.action.record.core.util.media

object MimeType {

    const val PLAIN_TEXT: String = "text/plain"
    const val JSON: String = "application/json"

    private const val IMAGE_TYPE: String = "image"

    fun getImageType(extension: String): String = "$IMAGE_TYPE/$extension"
    fun getExtensionType(mimeType: String?): String? = mimeType?.split('/')?.lastOrNull()
}
