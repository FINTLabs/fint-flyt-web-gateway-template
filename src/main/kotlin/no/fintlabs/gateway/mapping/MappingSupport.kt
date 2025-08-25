package no.fintlabs.gateway.mapping

internal fun MutableMap<String, String>.putOrEmpty(
    key: String,
    value: Any?,
) {
    put(key, value?.toString() ?: "")
}
