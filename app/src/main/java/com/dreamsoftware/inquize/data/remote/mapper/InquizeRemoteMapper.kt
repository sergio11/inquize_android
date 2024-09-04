package com.dreamsoftware.inquize.data.remote.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.InquizeDTO
import com.dreamsoftware.inquize.data.remote.dto.InquizeMessageDTO
import com.google.firebase.Timestamp

internal class InquizeRemoteMapper: IBrownieOneSideMapper<Map<String, Any?>, InquizeDTO> {

    private companion object {
        const val UID_KEY = "uid"
        const val USER_ID_KEY = "userId"
        const val IMAGE_URL_KEY = "imageUrl"
        const val IMAGE_DESCRIPTION_KEY = "imageUrl"
        const val CREATED_AT_KEY = "createdAt"
        const val MESSAGES_KEY = "messages"
        const val MESSAGE_ID_KEY = "uid"
        const val ROLE_KEY = "role"
        const val TEXT_KEY = "text"
    }

    override fun mapInToOut(input: Map<String, Any?>): InquizeDTO = with(input) {
        InquizeDTO(
            uid = get(UID_KEY) as String,
            userId = get(USER_ID_KEY) as String,
            imageUrl = get(IMAGE_URL_KEY) as String,
            imageDescription = get(IMAGE_DESCRIPTION_KEY) as String,
            createAt = get(CREATED_AT_KEY) as Timestamp,
            messages = (get(MESSAGES_KEY) as? List<Map<String, String>>)?.map {
                InquizeMessageDTO(
                    uid = it[MESSAGE_ID_KEY].orEmpty(),
                    role = it[ROLE_KEY].orEmpty(),
                    text = it[TEXT_KEY].orEmpty()
                )
            }.orEmpty()
        )
    }

    override fun mapInListToOutList(input: Iterable<Map<String, Any?>>): Iterable<InquizeDTO> =
        input.map(::mapInToOut)
}