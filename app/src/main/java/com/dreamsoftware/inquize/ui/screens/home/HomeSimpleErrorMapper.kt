package com.dreamsoftware.inquize.ui.screens.home

import android.content.Context
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.inquize.R

class HomeSimpleErrorMapper(
    private val context: Context
): IBrownieErrorMapper {
    override fun mapToMessage(ex: Throwable): String =
        context.getString(R.string.generic_error_exception)
}