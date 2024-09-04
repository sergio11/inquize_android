package com.dreamsoftware.inquize.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.domain.model.InquizeBO
import com.dreamsoftware.inquize.domain.model.InquizeMessageBO
import com.dreamsoftware.inquize.domain.model.InquizeMessageRoleEnum
import com.dreamsoftware.inquize.utils.enumNameOfOrDefault

internal class InquizeMapper : IBrownieOneSideMapper<InquizeDTO, InquizeBO> {

    override fun mapInListToOutList(input: Iterable<InquizeDTO>): Iterable<InquizeBO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: InquizeDTO): InquizeBO = with(input) {
        InquizeBO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            imageDescription = imageDescription,
            createAt = createAt.toDate(),
            question = messages.first().text,
            messages = messages.map {
                InquizeMessageBO(
                    uid = it.uid,
                    role = enumNameOfOrDefault(it.role, InquizeMessageRoleEnum.MODEL),
                    text = it.text
                )
            }
        )
    }
}