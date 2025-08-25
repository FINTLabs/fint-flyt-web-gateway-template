package no.fintlabs.gateway.simple

import no.fintlabs.gateway.model.SimpleExample
import no.fintlabs.gateway.webinstance.InstanceProcessor
import no.fintlabs.gateway.webinstance.InstanceProcessorFactoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SimpleProcessorConfiguration {
    @Bean(name = ["simpleProcessor"])
    fun simpleProcessor(
        instanceProcessorFactoryService: InstanceProcessorFactoryService,
        simpleMappingService: SimpleMappingService,
    ): InstanceProcessor<SimpleExample> {
        val idFunction: (SimpleExample) -> String? = { se ->
            se.sysId
        }

        return instanceProcessorFactoryService.createInstanceProcessor(
            "simpleExample",
            idFunction,
            simpleMappingService,
        )
    }
}
