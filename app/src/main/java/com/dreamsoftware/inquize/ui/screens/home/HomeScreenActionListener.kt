package com.dreamsoftware.inquize.ui.screens.home

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener
import com.dreamsoftware.inquize.domain.model.InquizeBO

interface HomeScreenActionListener: IBrownieScreenActionListener {
    fun onInquizeClicked(inquizeBO: InquizeBO)
    fun onSearchQueryUpdated(newSearchQuery: String)
}