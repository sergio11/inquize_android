package com.dreamsoftware.inquize.data.repository.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IImageDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IInquizeDataSource
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.SaveInquizeDTO
import com.dreamsoftware.inquize.data.remote.exception.SaveInquizeRemoteDataException
import com.dreamsoftware.inquize.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.inquize.domain.exception.SaveInquizeException
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.SaveInquizeBO
import com.dreamsoftware.inquize.domain.repository.IInquizeRepository
import kotlinx.coroutines.CoroutineDispatcher

internal class InquizeRepositoryImpl(
    private val inquizeDataSource: IInquizeDataSource,
    private val imageDataSource: IImageDataSource,
    private val saveInquizeMapper: IBrownieOneSideMapper<SaveInquizeBO, SaveInquizeDTO>,
    private val inquizeMapper: IBrownieOneSideMapper<InquizeDTO, InquizeBO>,
    dispatcher: CoroutineDispatcher
): SupportRepositoryImpl(dispatcher), IInquizeRepository {

    @Throws(SaveInquizeException::class)
    override suspend fun save(data: SaveInquizeBO): InquizeBO = safeExecute {
        try {
            with(inquizeDataSource) {
                val newImageUrl = imageDataSource.save(path = data.imageUrl, name = data.uid)
                save(saveInquizeMapper.mapInToOut(data.copy(imageUrl = newImageUrl)))
                inquizeMapper.mapInToOut(fetchById(userId = data.userId, id = data.uid))
            }
        } catch (ex: SaveInquizeRemoteDataException) {
            throw SaveInquizeException("An error occurred when trying to save inquize", ex)
        }
    }
}