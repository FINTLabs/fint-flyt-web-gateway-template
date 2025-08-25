package no.fintlabs.gateway.model

data class AdvancedExample(
    val sysId: String,
    val string1: String,
    val int1: Int,
    val dokumenter: List<Dokument>,
    val saksbehandlere: List<Saksbehandler>,
)
