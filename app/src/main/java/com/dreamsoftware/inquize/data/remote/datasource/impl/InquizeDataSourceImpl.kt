package com.dreamsoftware.inquize.data.remote.datasource.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IInquizeDataSource
import com.dreamsoftware.inquize.data.remote.dto.CreateInquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.exception.DeleteInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchAllInquizeRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.CreateInquizeRemoteDataException
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class InquizeDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val saveInquizeMapper: IBrownieOneSideMapper<CreateInquizeDTO, Map<String, Any?>>,
    private val inquizeMapper: IBrownieOneSideMapper<Map<String, Any?>, InquizeDTO>,
    private val dispatcher: CoroutineDispatcher
): IInquizeDataSource {

    private companion object {
        const val COLLECTION_NAME = "user_questions"
        const val SUB_COLLECTION_NAME = "questions"
    }

    private val userQuestionsCollection by lazy {
        firestore.collection(COLLECTION_NAME)
    }

    @Throws(CreateInquizeRemoteDataException::class)
    override suspend fun create(data: CreateInquizeDTO): Unit = withContext(dispatcher) {
        try {
            userQuestionsCollection
                .document(data.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(data.uid)
                .set(saveInquizeMapper.mapInToOut(data))
                .await()
        } catch (e: Exception) {
            throw CreateInquizeRemoteDataException("Failed to save user question", e)
        }
    }

    @Throws(FetchInquizeByIdRemoteDataException::class)
    override suspend fun fetchById(userId: String, id: String): InquizeDTO =
        withContext(dispatcher) {
            try {
                val document = userQuestionsCollection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .document(id)
                    .get()
                    .await()
                inquizeMapper.mapInToOut(
                    document.data ?: throw FetchInquizeByIdRemoteDataException("Document data is null")
                )
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw FetchInquizeByIdRemoteDataException(
                    "An error occurred when trying to fetch the user question with ID $id",
                    ex
                )
            }
        }

    @Throws(FetchAllInquizeRemoteDataException::class)
    override suspend fun fetchAllByUserId(userId: String): List<InquizeDTO> =
        withContext(dispatcher) {
            try {
                val snapshot = userQuestionsCollection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .get()
                    .await()
                snapshot.documents.map { document ->
                    inquizeMapper.mapInToOut(
                        document.data ?: throw FetchAllInquizeRemoteDataException("Document data is null")
                    )
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw FetchAllInquizeRemoteDataException(
                    "An error occurred when trying to fetch all user questions",
                    ex
                )
            }
        }

    @Throws(DeleteInquizeByIdRemoteDataException::class)
    override suspend fun deleteById(userId: String, id: String): Unit =
        withContext(dispatcher) {
            try {
                userQuestionsCollection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .document(id)
                    .delete()
                    .await()
            } catch (ex: Exception) {
                throw DeleteInquizeByIdRemoteDataException(
                    "An error occurred when trying to delete the user question",
                    ex
                )
            }
        }
}