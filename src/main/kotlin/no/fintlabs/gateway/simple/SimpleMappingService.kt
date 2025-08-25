package no.fintlabs.gateway.simple

import no.fintlabs.gateway.mapping.putOrEmpty
import no.fintlabs.gateway.model.SimpleExample
import no.fintlabs.gateway.webinstance.InstanceMapper
import no.fintlabs.gateway.webinstance.model.File
import no.fintlabs.gateway.webinstance.model.instance.InstanceObject
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SimpleMappingService : InstanceMapper<SimpleExample> {
    override fun map(
        @Suppress("UNUSED_PARAMETER") sourceApplicationId: Long,
        incomingInstance: SimpleExample,
        @Suppress("UNUSED_PARAMETER") persistFile: (File) -> UUID,
    ): InstanceObject {
        return with(incomingInstance) {
            val valuePerKey =
                buildMap {
                    putOrEmpty("sysId", sysId)
                    putOrEmpty("string1", string1)
                    putOrEmpty("int1", int1)
                }

            InstanceObject(valuePerKey)
        }
    }
}
