package com.dreamsoftware.inquize.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.inquize.domain.model.ResolveQuestionBO

internal class ResolveQuestionMapper : IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO> {
    override fun mapInListToOutList(input: Iterable<ResolveQuestionBO>): Iterable<ResolveQuestionDTO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: ResolveQuestionBO): ResolveQuestionDTO = with(input) {
        ResolveQuestionDTO(
            question = question,
            imageUrl = imageUrl,
            history = history
        )
    }
}