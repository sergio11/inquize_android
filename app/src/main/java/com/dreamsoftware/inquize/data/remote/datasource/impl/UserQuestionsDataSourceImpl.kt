package com.dreamsoftware.inquize.data.remote.datasource.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IUserQuestionsDataSource
import com.dreamsoftware.inquize.data.remote.dto.SaveUserQuestionDTO
import com.dreamsoftware.inquize.data.remote.dto.UserQuestionDTO
import com.dreamsoftware.inquize.data.remote.exception.DeleteUserQuestionByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.GetAllUserQuestionsRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.GetUserQuestionByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SaveUserQuestionRemoteDataException
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

internal class UserQuestionsDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val saveUserQuestionMapper: IBrownieOneSideMapper<SaveUserQuestionDTO, Map<String, Any?>>,
    private val userQuestionMapper: IBrownieOneSideMapper<Map<String, Any?>, UserQuestionDTO>,
    private val dispatcher: CoroutineDispatcher
): IUserQuestionsDataSource {

    private companion object {
        const val COLLECTION_NAME = "user_questions"
        const val SUB_COLLECTION_NAME = "questions"
    }

    private val userQuestionsCollection by lazy {
        firestore.collection(COLLECTION_NAME)
    }

    @Throws(SaveUserQuestionRemoteDataException::class)
    override suspend fun saveUserQuestion(userQuestion: SaveUserQuestionDTO): Unit = withContext(dispatcher) {
        try {
            userQuestionsCollection
                .document(userQuestion.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(userQuestion.uid)
                .set(saveUserQuestionMapper.mapInToOut(userQuestion))
                .await()
        } catch (e: Exception) {
            throw SaveUserQuestionRemoteDataException("Failed to save user question", e)
        }
    }

    @Throws(GetUserQuestionByIdRemoteDataException::class)
    override suspend fun fetchUserQuestionById(userId: String, questionId: String): UserQuestionDTO =
        withContext(dispatcher) {
            try {
                val document = userQuestionsCollection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .document(questionId)
                    .get()
                    .await()
                userQuestionMapper.mapInToOut(
                    document.data ?: throw GetUserQuestionByIdRemoteDataException("Document data is null")
                )
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw GetUserQuestionByIdRemoteDataException(
                    "An error occurred when trying to fetch the user question with ID $questionId",
                    ex
                )
            }
        }

    @Throws(GetAllUserQuestionsRemoteDataException::class)
    override suspend fun fetchAllUserQuestions(userId: String): List<UserQuestionDTO> =
        withContext(dispatcher) {
            try {
                val snapshot = userQuestionsCollection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .get()
                    .await()
                snapshot.documents.map { document ->
                    userQuestionMapper.mapInToOut(
                        document.data ?: throw GetAllUserQuestionsRemoteDataException("Document data is null")
                    )
                }
            } catch (ex: FirebaseException) {
                throw ex
            } catch (ex: Exception) {
                throw GetAllUserQuestionsRemoteDataException(
                    "An error occurred when trying to fetch all user questions",
                    ex
                )
            }
        }

    @Throws(DeleteUserQuestionByIdRemoteDataException::class)
    override suspend fun deleteUserQuestionById(userId: String, questionId: String): Unit =
        withContext(dispatcher) {
            try {
                userQuestionsCollection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .document(questionId)
                    .delete()
                    .await()
            } catch (ex: Exception) {
                throw DeleteUserQuestionByIdRemoteDataException(
                    "An error occurred when trying to delete the user question",
                    ex
                )
            }
        }
}