package com.dreamsoftware.inquize.ui.screens.create

import android.content.Context
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.inquize.R
import com.dreamsoftware.inquize.domain.exception.InvalidDataException

class CreateInquizeScreenSimpleErrorMapper(
    private val context: Context
): IBrownieErrorMapper {
    override fun mapToMessage(ex: Throwable): String = context.getString(when(ex) {
        is InvalidDataException -> R.string.generic_form_invalid_data_provided
        else -> R.string.generic_error_exception
    })
}