package com.dreamsoftware.inquize.data.remote.datasource

import com.dreamsoftware.inquize.data.remote.dto.SaveUserQuestionDTO
import com.dreamsoftware.inquize.data.remote.dto.UserQuestionDTO
import com.dreamsoftware.inquize.data.remote.exception.DeleteUserQuestionByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.GetAllUserQuestionsRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.GetUserQuestionByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SaveUserQuestionRemoteDataException

interface IUserQuestionsDataSource {

    @Throws(SaveUserQuestionRemoteDataException::class)
    suspend fun saveUserQuestion(userQuestion: SaveUserQuestionDTO)

    @Throws(GetUserQuestionByIdRemoteDataException::class)
    suspend fun fetchUserQuestionById(userId: String, questionId: String): UserQuestionDTO

    @Throws(GetAllUserQuestionsRemoteDataException::class)
    suspend fun fetchAllUserQuestions(userId: String): List<UserQuestionDTO>

    @Throws(DeleteUserQuestionByIdRemoteDataException::class)
    suspend fun deleteUserQuestionById(userId: String, questionId: String)
}