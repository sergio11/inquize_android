package com.dreamsoftware.inquize.data.remote.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.CreateInquizeDTO
import com.google.firebase.Timestamp
import java.util.Date

internal class CreateInquizeRemoteMapper: IBrownieOneSideMapper<CreateInquizeDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val USER_ID_KEY = "userId"
        const val IMAGE_URL_KEY = "imageUrl"
        const val CREATED_AT_KEY = "createdAt"
        const val MESSAGES_KEY = "messages"
        const val USER_ROLE_KEY = "USER"
        const val SYSTEM_ROLE_KEY = "SYSTEM"
        const val ROLE_KEY = "role"
        const val TEXT_KEY = "text"
    }

    override fun mapInToOut(input: CreateInquizeDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            USER_ID_KEY to userId,
            IMAGE_URL_KEY to imageUrl,
            CREATED_AT_KEY to Timestamp(Date()),
            MESSAGES_KEY to listOf(
                hashMapOf(
                    ROLE_KEY to USER_ROLE_KEY,
                    TEXT_KEY to question
                ),
                hashMapOf(
                    ROLE_KEY to SYSTEM_ROLE_KEY,
                    TEXT_KEY to answer
                )
            )
        )
    }

    override fun mapInListToOutList(input: Iterable<CreateInquizeDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}