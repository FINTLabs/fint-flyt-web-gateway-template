package no.novari.flyt.example.gateway.instance.advanced

import no.novari.flyt.example.gateway.instance.model.AdvancedExample
import no.novari.flyt.gateway.instance.InstanceProcessor
import no.novari.flyt.gateway.instance.InstanceProcessorFactoryService
import no.novari.flyt.gateway.instance.MultipartInstanceProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdvancedProcessorConfiguration {
    @Bean(name = ["advancedProcessor"])
    fun advancedProcessor(
        instanceProcessorFactoryService: InstanceProcessorFactoryService,
        advancedMappingService: AdvancedMappingService,
    ): InstanceProcessor<AdvancedExample> {
        return instanceProcessorFactoryService.createInstanceProcessor(
            "advancedExample",
            { ae -> ae.sysId },
            advancedMappingService,
        )
    }

    @Bean(name = ["advancedMultipartProcessor"])
    fun advancedMultipartProcessor(
        instanceProcessorFactoryService: InstanceProcessorFactoryService,
        advancedMultipartMappingService: AdvancedMultipartMappingService,
    ): MultipartInstanceProcessor<AdvancedExample> {
        return instanceProcessorFactoryService.createMultipartInstanceProcessor(
            "advancedExample",
            { ae -> ae.sysId },
            advancedMultipartMappingService,
        )
    }
}
