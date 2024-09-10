package com.dreamsoftware.inquize.data.remote.datasource.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IInquizeDataSource
import com.dreamsoftware.inquize.data.remote.datasource.impl.core.SupportDataSourceImpl
import com.dreamsoftware.inquize.data.remote.dto.AddInquizeMessageDTO
import com.dreamsoftware.inquize.data.remote.dto.CreateInquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.exception.AddInquizeMessageRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.CreateInquizeRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.DeleteInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchAllInquizeRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SearchInquizeRemoteDataException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await

internal class InquizeDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val saveInquizeMapper: IBrownieOneSideMapper<CreateInquizeDTO, Map<String, Any?>>,
    private val addInquizeMessageMapper: IBrownieOneSideMapper<AddInquizeMessageDTO, List<Map<String, String>>>,
    private val inquizeMapper: IBrownieOneSideMapper<Map<String, Any?>, InquizeDTO>,
    dispatcher: CoroutineDispatcher
): SupportDataSourceImpl(dispatcher), IInquizeDataSource {

    private companion object {
        const val COLLECTION_NAME = "user_inquize"
        const val SUB_COLLECTION_NAME = "questions"
        const val MESSAGE_FIELD_NAME = "messages"
        const val IMAGE_DESCRIPTION_FIELD_NAME = "imageDescription"
        const val CREATED_AT_FIELD_NAME = "createdAt"
    }

    private val inquizeCollection by lazy {
        firestore.collection(COLLECTION_NAME)
    }

    @Throws(SearchInquizeRemoteDataException::class)
    override suspend fun search(userId: String, term: String): List<InquizeDTO> = safeExecution(
        onExecuted = {
            val snapshot = inquizeCollection
                .document(userId)
                .collection(SUB_COLLECTION_NAME)
                .whereGreaterThanOrEqualTo(IMAGE_DESCRIPTION_FIELD_NAME, term)
                .whereLessThanOrEqualTo(IMAGE_DESCRIPTION_FIELD_NAME, term + "\uf8ff")
                .orderBy(CREATED_AT_FIELD_NAME, Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.map { document ->
                inquizeMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            }
        },
        onErrorOccurred = { ex ->
            SearchInquizeRemoteDataException("Failed to search questions", ex)
        }
    )

    @Throws(CreateInquizeRemoteDataException::class)
    override suspend fun create(data: CreateInquizeDTO): Unit = safeExecution(
        onExecuted = {
            inquizeCollection
                .document(data.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(data.uid)
                .set(saveInquizeMapper.mapInToOut(data))
                .await()
        },
        onErrorOccurred = { ex ->
            CreateInquizeRemoteDataException("Failed to save user question", ex)
        }
    )

    @Throws(AddInquizeMessageRemoteDataException::class)
    override suspend fun addMessage(data: AddInquizeMessageDTO): Unit = safeExecution(
        onExecuted = {
            val messages = addInquizeMessageMapper.mapInToOut(data)
            val documentRef = inquizeCollection
                .document(data.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(data.uid)
            for (message in messages) {
                documentRef.update(MESSAGE_FIELD_NAME, FieldValue.arrayUnion(message)).await()
            }
        },
        onErrorOccurred = { ex ->
            AddInquizeMessageRemoteDataException("Failed to save user question", ex)
        }
    )

    @Throws(FetchInquizeByIdRemoteDataException::class)
    override suspend fun fetchById(userId: String, id: String): InquizeDTO =
        safeExecution(
            onExecuted = {
                val document = inquizeCollection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .document(id)
                    .get()
                    .await()
                inquizeMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            },
            onErrorOccurred = { ex ->
                FetchInquizeByIdRemoteDataException(
                    "An error occurred when trying to fetch the user question with ID $id",
                    ex
                )
            }
        )

    @Throws(FetchAllInquizeRemoteDataException::class)
    override suspend fun fetchAllByUserId(userId: String): List<InquizeDTO> = safeExecution(
        onExecuted = {
            val snapshot = inquizeCollection
                .document(userId)
                .collection(SUB_COLLECTION_NAME)
                .orderBy(CREATED_AT_FIELD_NAME, Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.map { document ->
                inquizeMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            }
        },
        onErrorOccurred = { ex ->
            FetchAllInquizeRemoteDataException(
                "An error occurred when trying to fetch all user questions",
                ex
            )
        }
    )

    @Throws(DeleteInquizeByIdRemoteDataException::class)
    override suspend fun deleteById(userId: String, id: String): Unit = safeExecution(
        onExecuted = {
            inquizeCollection
                .document(userId)
                .collection(SUB_COLLECTION_NAME)
                .document(id)
                .delete()
                .await()
        },
        onErrorOccurred = { ex ->
            DeleteInquizeByIdRemoteDataException(
                "An error occurred when trying to delete the user question",
                ex
            )
        }
    )
}