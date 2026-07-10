package no.novari.flyt.example.gateway.instance.model

import no.novari.flyt.gateway.instance.model.MultipartFileReference

data class Document(
    val title: String,
    val fileContent: FileContent? = null,
    val multipartFileReference: MultipartFileReference? = null,
)
