package com.dreamsoftware.inquize

import android.app.Application
import com.dreamsoftware.inquize.utils.IInquizeApplicationAware
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InquizeApplication : Application(), IInquizeApplicationAware