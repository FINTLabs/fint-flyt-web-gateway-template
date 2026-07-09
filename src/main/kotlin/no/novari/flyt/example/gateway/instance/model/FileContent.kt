package no.novari.flyt.example.gateway.instance.model

data class FileContent(
    val fileName: String,
    val mimeType: String,
    @Suppress("ArrayInDataClass")
    val base64: ByteArray,
)
