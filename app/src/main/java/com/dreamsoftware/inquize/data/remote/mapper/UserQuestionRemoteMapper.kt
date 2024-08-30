package com.dreamsoftware.inquize.data.remote.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.google.firebase.Timestamp

internal class UserQuestionRemoteMapper: IBrownieOneSideMapper<Map<String, Any?>, InquizeDTO> {

    private companion object {
        const val UID_KEY = "uid"
        const val USER_ID_KEY = "userId"
        const val IMAGE_URL_KEY = "imageUrl"
        const val PROMPT_KEY = "prompt"
        const val CREATED_AT_KEY = "createdAt"
        const val MESSAGES_KEY = "messages"
    }

    override fun mapInToOut(input: Map<String, Any?>): InquizeDTO = with(input) {
        InquizeDTO(
            uid = get(UID_KEY) as String,
            userId = get(USER_ID_KEY) as String,
            imageUrl = get(IMAGE_URL_KEY) as String,
            prompt = get(PROMPT_KEY) as String,
            createAt = get(CREATED_AT_KEY) as Timestamp,
            messages = get(MESSAGES_KEY) as List<String>
        )
    }

    override fun mapInListToOutList(input: Iterable<Map<String, Any?>>): Iterable<InquizeDTO> =
        input.map(::mapInToOut)
}