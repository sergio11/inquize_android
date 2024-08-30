package com.dreamsoftware.inquize.data.remote.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.inquize.data.remote.dto.SaveInquizeDTO
import com.google.firebase.Timestamp
import java.util.Date

internal class SaveUserQuestionRemoteMapper: IBrownieOneSideMapper<SaveInquizeDTO, Map<String, Any?>> {

    private companion object {
        const val UID_KEY = "uid"
        const val USER_ID_KEY = "userId"
        const val IMAGE_URL_KEY = "imageUrl"
        const val CREATED_AT_KEY = "createdAt"
        const val MESSAGES_KEY = "messages"
    }

    override fun mapInToOut(input: SaveInquizeDTO): Map<String, Any?> = with(input) {
        hashMapOf(
            UID_KEY to uid,
            USER_ID_KEY to userId,
            IMAGE_URL_KEY to imageUrl,
            CREATED_AT_KEY to Timestamp(Date()),
            MESSAGES_KEY to listOf("USER" to question)
        )
    }

    override fun mapInListToOutList(input: Iterable<SaveInquizeDTO>): Iterable<Map<String, Any?>> =
        input.map(::mapInToOut)
}