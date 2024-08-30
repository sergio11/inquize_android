package com.dreamsoftware.inquize.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.domain.model.InquizeBO

internal class InquizeMapper : IBrownieOneSideMapper<InquizeDTO, InquizeBO> {

    override fun mapInListToOutList(input: Iterable<InquizeDTO>): Iterable<InquizeBO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: InquizeDTO): InquizeBO = with(input) {
        InquizeBO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            prompt = prompt,
            createAt = createAt.toDate(),
            messages = messages
        )
    }
}