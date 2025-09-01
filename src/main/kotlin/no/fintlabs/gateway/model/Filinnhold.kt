package no.fintlabs.gateway.model

data class Filinnhold(
    val filnavn: String,
    val mimeType: String,
    @Suppress("ArrayInDataClass")
    val base64: ByteArray,
)
