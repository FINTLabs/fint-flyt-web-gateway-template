package no.novari.flyt.example.gateway.instance.advanced

import no.novari.flyt.example.gateway.instance.mapping.putOrEmpty
import no.novari.flyt.example.gateway.instance.model.AdvancedExample
import no.novari.flyt.example.gateway.instance.model.CaseWorker
import no.novari.flyt.example.gateway.instance.model.Document
import no.novari.flyt.example.gateway.instance.model.FileContent
import no.novari.flyt.gateway.instance.InstanceMapper
import no.novari.flyt.gateway.instance.model.File
import no.novari.flyt.gateway.instance.model.instance.InstanceObject
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
class AdvancedMappingService : InstanceMapper<AdvancedExample> {
    override fun map(
        sourceApplicationId: Long,
        incomingInstance: AdvancedExample,
        persistFile: (File) -> UUID,
    ): InstanceObject {
        return with(incomingInstance) {
            val documentInstanceObjects =
                mapAttachmentDocumentsToInstanceObjects(
                    persistFile = persistFile,
                    sourceApplicationId = sourceApplicationId,
                    sourceApplicationInstanceId = incomingInstance.sysId,
                    documents = documents,
                )

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

    private fun mapAttachmentDocumentsToInstanceObjects(
        persistFile: (File) -> UUID,
        sourceApplicationId: Long,
        sourceApplicationInstanceId: String,
        documents: List<Document>,
    ): List<InstanceObject> {
        return documents.map {
            mapAttachmentDocumentToInstanceObject(
                persistFile = persistFile,
                sourceApplicationId = sourceApplicationId,
                sourceApplicationInstanceId = sourceApplicationInstanceId,
                document = it,
            )
        }
    }

    private fun getMediaTypeFromFileContent(fileContent: FileContent): MediaType {
        fileContent.mimeType
            .takeIf(String::isNotBlank)
            ?.let(MediaType::parseMediaType)
            ?.let { return it }

        val mediaType = MediaTypeFactory.getMediaType(fileContent.fileName)
        return mediaType.orElseThrow {
            IllegalArgumentException("No media type found for fileName=${fileContent.fileName}")
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun toFile(
        sourceApplicationId: Long,
        sourceApplicationInstanceId: String,
        fileContent: FileContent,
        mediaType: MediaType,
    ): File {
        return File(
            name = fileContent.fileName,
            type = mediaType,
            sourceApplicationId = sourceApplicationId,
            sourceApplicationInstanceId = sourceApplicationInstanceId,
            encoding = "UTF-8",
            base64Contents = Base64.encode(fileContent.base64),
        )
    }

    private fun mapAttachmentDocumentToInstanceObject(
        persistFile: (File) -> UUID,
        sourceApplicationId: Long,
        sourceApplicationInstanceId: String,
        document: Document,
    ): InstanceObject {
        val fileContent =
            requireNotNull(document.fileContent) {
                "Document '${document.title}' must contain fileContent when advanced JSON endpoint is used"
            }
        val mediaType = getMediaTypeFromFileContent(fileContent)
        val file = toFile(sourceApplicationId, sourceApplicationInstanceId, fileContent, mediaType)
        val fileId = persistFile(file)
        return mapAttachmentDocumentAndFileIdToInstanceObject(
            fileName = fileContent.fileName,
            mediaType = mediaType.toString(),
            fileId = fileId,
        )
    }

    private fun mapAttachmentDocumentAndFileIdToInstanceObject(
        fileName: String,
        mediaType: String?,
        fileId: UUID,
    ): InstanceObject {
        return InstanceObject(
            valuePerKey =
                buildMap {
                    putOrEmpty("title", fileName)
                    putOrEmpty("fileName", fileName)
                    putOrEmpty("mediaType", mediaType)
                    putOrEmpty("file", fileId)
                },
        )
    }
}
