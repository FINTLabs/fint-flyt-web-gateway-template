package no.novari.flyt.example.gateway.instance.advanced

import no.novari.flyt.example.gateway.instance.model.AdvancedExample
import no.novari.flyt.example.gateway.instance.model.CaseWorker
import no.novari.flyt.example.gateway.instance.model.Document
import no.novari.flyt.gateway.instance.model.MultipartFileReference
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import java.util.UUID

class AdvancedMultipartMappingServiceTest {
    private val service = AdvancedMultipartMappingService()

    @Test
    fun `maps advanced multipart example with file references and case workers`() {
        val fileReference =
            MultipartFileReference(
                partName = "documents",
                fileName = "attachment.pdf",
                originalFilename = "attachment.pdf",
                type = MediaType.APPLICATION_PDF,
            )
        val document =
            Document(
                title = "Original title",
                multipartFileReference = fileReference,
            )
        val caseWorker = CaseWorker(email = "caseworker@example.com", name = "Case Worker")
        val input =
            AdvancedExample(
                sysId = "sys-adv-1",
                string1 = "hello",
                int1 = 7,
                documents = listOf(document),
                caseWorkers = listOf(caseWorker),
            )

        val capturedFileReferences = mutableListOf<MultipartFileReference>()
        val expectedFileId = UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6")

        val result =
            service.map(
                sourceApplicationId = 99L,
                incomingInstance = input,
                persistFile = { reference ->
                    capturedFileReferences += reference
                    expectedFileId
                },
            )

        assertEquals(1, capturedFileReferences.size)
        assertSame(fileReference, capturedFileReferences.single())

        assertEquals(
            mapOf(
                "sysId" to "sys-adv-1",
                "string1" to "hello",
                "int1" to "7",
            ),
            result.valuePerKey,
        )

        val documentObject = result.objectCollectionPerKey.getValue("documents").single()
        assertEquals(
            mapOf(
                "title" to "attachment.pdf",
                "fileName" to "attachment.pdf",
                "mediaType" to "application/pdf",
                "file" to expectedFileId.toString(),
            ),
            documentObject.valuePerKey,
        )

        val caseWorkerObject = result.objectCollectionPerKey.getValue("caseWorkers").single()
        assertEquals(
            mapOf(
                "email" to "caseworker@example.com",
                "name" to "Case Worker",
            ),
            caseWorkerObject.valuePerKey,
        )

        assertTrue(documentObject.objectCollectionPerKey.isEmpty())
        assertTrue(caseWorkerObject.objectCollectionPerKey.isEmpty())
    }
}
