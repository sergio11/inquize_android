package com.dreamsoftware.inquize.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.SaveInquizeDTO
import com.dreamsoftware.inquize.domain.model.SaveInquizeBO

internal class SaveInquizeMapper: IBrownieOneSideMapper<SaveInquizeBO, SaveInquizeDTO> {
    override fun mapInListToOutList(input: Iterable<SaveInquizeBO>): Iterable<SaveInquizeDTO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: SaveInquizeBO): SaveInquizeDTO = with(input) {
        SaveInquizeDTO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            question = question
        )
    }
}