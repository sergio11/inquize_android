package com.dreamsoftware.inquize.data.repository.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.datasource.IImageDataSource
import com.dreamsoftware.inquize.data.remote.datasource.IInquizeDataSource
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.SaveInquizeDTO
import com.dreamsoftware.inquize.data.remote.exception.DeleteInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchAllInquizeRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.FetchInquizeByIdRemoteDataException
import com.dreamsoftware.inquize.data.remote.exception.SaveInquizeRemoteDataException
import com.dreamsoftware.inquize.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.inquize.domain.exception.DeleteInquizeByIdException
import com.dreamsoftware.inquize.domain.exception.FetchAllInquizeException
import com.dreamsoftware.inquize.domain.exception.FetchInquizeByIdException
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
            ex.printStackTrace()
            throw SaveInquizeException("An error occurred when trying to save inquize", ex)
        }
    }

    @Throws(DeleteInquizeByIdException::class)
    override suspend fun deleteById(userId: String, id: String) {
        safeExecute {
            try {
                imageDataSource.deleteByName(name = id)
                inquizeDataSource.deleteById(userId = userId, id = id)
            } catch (ex: DeleteInquizeByIdRemoteDataException) {
                ex.printStackTrace()
                throw DeleteInquizeByIdException("An error occurred when trying to delete the inquize", ex)
            }
        }
    }

    @Throws(FetchInquizeByIdException::class)
    override suspend fun fetchById(userId: String, id: String): InquizeBO = safeExecute {
        try {
            inquizeDataSource.fetchById(userId = userId, id = id)
                .let(inquizeMapper::mapInToOut)
        } catch (ex: FetchInquizeByIdRemoteDataException) {
            ex.printStackTrace()
            throw FetchInquizeByIdException("An error occurred when trying to fetch the inquize data", ex)
        }
    }

    @Throws(FetchAllInquizeException::class)
    override suspend fun fetchAllByUserId(userId: String): List<InquizeBO> = safeExecute {
        try {
            inquizeDataSource.fetchAllByUserId(userId = userId)
                .let(inquizeMapper::mapInListToOutList)
                .toList()
        } catch (ex: FetchAllInquizeRemoteDataException) {
            ex.printStackTrace()
            throw FetchAllInquizeException("An error occurred when trying to fetch all user questions", ex)
        }
    }
}