package no.novari.flyt.example.gateway.instance.advanced

import no.novari.flyt.example.gateway.instance.mapping.putOrEmpty
import no.novari.flyt.example.gateway.instance.model.AdvancedExample
import no.novari.flyt.example.gateway.instance.model.CaseWorker
import no.novari.flyt.example.gateway.instance.model.Document
import no.novari.flyt.gateway.instance.MultipartInstanceMapper
import no.novari.flyt.gateway.instance.model.MultipartFileReference
import no.novari.flyt.gateway.instance.model.instance.InstanceObject
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AdvancedMultipartMappingService : MultipartInstanceMapper<AdvancedExample> {
    override fun map(
        sourceApplicationId: Long,
        incomingInstance: AdvancedExample,
        persistFile: (MultipartFileReference) -> UUID,
    ): InstanceObject {
        return with(incomingInstance) {
            val documentInstanceObjects =
                documents.map { document ->
                    mapAttachmentDocumentToInstanceObject(
                        persistFile = persistFile,
                        document = document,
                    )
                }

            val objectCollectionPerKey =
                mutableMapOf<String, Collection<InstanceObject>>(
                    "documents" to documentInstanceObjects,
                    "caseWorkers" to mapCaseWorkersToInstanceObjects(caseWorkers),
                )

            InstanceObject(
                valuePerKey =
                    buildMap {
                        putOrEmpty("sysId", sysId)
                        putOrEmpty("string1", string1)
                        putOrEmpty("int1", int1)
                    },
                objectCollectionPerKey = objectCollectionPerKey,
            )
        }
    }

    private fun mapCaseWorkersToInstanceObjects(caseWorkers: List<CaseWorker>): List<InstanceObject> {
        return caseWorkers.map(::mapCaseWorkerToInstanceObject)
    }

    private fun mapCaseWorkerToInstanceObject(caseWorker: CaseWorker): InstanceObject {
        return InstanceObject(
            valuePerKey =
                buildMap {
                    putOrEmpty("email", caseWorker.email)
                    putOrEmpty("name", caseWorker.name)
                },
        )
    }

    private fun mapAttachmentDocumentToInstanceObject(
        persistFile: (MultipartFileReference) -> UUID,
        document: Document,
    ): InstanceObject {
        val fileReference =
            requireNotNull(document.multipartFileReference) {
                "Document '${document.title}' must contain multipartFileReference when advanced multipart endpoint is used"
            }

        val fileId = persistFile(fileReference)
        val fileName = fileReference.fileName ?: fileReference.originalFilename ?: fileReference.partName

        return InstanceObject(
            valuePerKey =
                buildMap {
                    putOrEmpty("title", fileName)
                    putOrEmpty("fileName", fileName)
                    putOrEmpty("mediaType", fileReference.type)
                    putOrEmpty("file", fileId)
                },
        )
    }
}
