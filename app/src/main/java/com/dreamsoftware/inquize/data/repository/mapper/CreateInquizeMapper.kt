package com.dreamsoftware.inquize.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.CreateInquizeDTO
import com.dreamsoftware.inquize.domain.model.CreateInquizeBO

internal class CreateInquizeMapper: IBrownieOneSideMapper<CreateInquizeBO, CreateInquizeDTO> {
    override fun mapInListToOutList(input: Iterable<CreateInquizeBO>): Iterable<CreateInquizeDTO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: CreateInquizeBO): CreateInquizeDTO = with(input) {
        CreateInquizeDTO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            question = question,
            answer = answer
        )
    }
}