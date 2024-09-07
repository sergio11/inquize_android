package com.dreamsoftware.inquize

import android.app.Application
import com.dreamsoftware.brownie.utils.IBrownieAppEvent
import com.dreamsoftware.inquize.utils.IInquizeApplicationAware
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InquizeApplication : Application(), IInquizeApplicationAware

sealed interface AppEvent: IBrownieAppEvent {
    data object SignOff: AppEvent
}