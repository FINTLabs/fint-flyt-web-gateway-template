package no.fintlabs.gateway

import no.fintlabs.gateway.model.AdvancedExample
import no.fintlabs.gateway.model.SimpleExample
import no.fintlabs.gateway.model.SimpleExampleStatus
import no.fintlabs.gateway.webinstance.InstanceProcessor
import no.fintlabs.webresourceserver.UrlPaths.EXTERNAL_API
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("${EXTERNAL_API}example/instances")
class ExampleController(
    @param:Qualifier("simpleProcessor")
    private val simpleExampleProcessor: InstanceProcessor<SimpleExample>,
    private val advancedExampleProcessor: InstanceProcessor<AdvancedExample>,
) {
    @GetMapping("/simple/{instanceId}/status")
    fun getSimpleStatus(
        @PathVariable("instanceId") instanceId: String,
    ): SimpleExampleStatus {
        return SimpleExampleStatus(instanceId)
    }

    @PostMapping("/simple")
    fun createSimpleExample(
        @RequestBody simpleExample: SimpleExample,
        @AuthenticationPrincipal authentication: Authentication,
    ): ResponseEntity<Void> {
        return simpleExampleProcessor.processInstance(authentication, simpleExample)
    }

    @PostMapping("/advanced")
    fun createAdvancedExample(
        @RequestBody advancedExample: AdvancedExample,
        @AuthenticationPrincipal authentication: Authentication,
    ): ResponseEntity<Void> {
        return advancedExampleProcessor.processInstance(authentication, advancedExample)
    }
}
