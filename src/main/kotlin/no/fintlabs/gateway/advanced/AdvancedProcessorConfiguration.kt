package no.fintlabs.gateway.advanced

import no.fintlabs.gateway.model.AdvancedExample
import no.fintlabs.gateway.webinstance.InstanceProcessor
import no.fintlabs.gateway.webinstance.InstanceProcessorFactoryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdvancedProcessorConfiguration {
    @Bean(name = ["advancedProcessor"])
    fun advancedProcessor(
        instanceProcessorFactoryService: InstanceProcessorFactoryService,
        advancedMappingService: AdvancedMappingService,
    ): InstanceProcessor<AdvancedExample> {
        val idFunction: (AdvancedExample) -> String? = { ae ->
            ae.sysId
        }

        return instanceProcessorFactoryService.createInstanceProcessor(
            "advancedExample",
            idFunction,
            advancedMappingService,
        )
    }
}
