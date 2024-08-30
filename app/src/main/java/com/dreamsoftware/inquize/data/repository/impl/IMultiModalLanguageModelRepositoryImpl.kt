package com.dreamsoftware.inquize.data.repository.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.inquize.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO
import com.dreamsoftware.inquize.domain.repository.IMultiModalLanguageModelRepository
import kotlinx.coroutines.CoroutineDispatcher

internal class IMultiModalLanguageModelRepositoryImpl(
    private val multiModalLanguageModelDataSource: IMultiModalLanguageModelDataSource,
    private val resolveQuestionMapper: IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO>,
    dispatcher: CoroutineDispatcher
): SupportRepositoryImpl(dispatcher), IMultiModalLanguageModelRepository {

    override suspend fun resolveQuestion(data: ResolveQuestionBO): String = safeExecute {
        multiModalLanguageModelDataSource.resolveQuestion(resolveQuestionMapper.mapInToOut(data))
    }
}