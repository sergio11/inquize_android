package sanchez.sanchez.sergio.brownie.network.interceptors

import android.content.Context
import android.util.Log
import sanchez.sanchez.sergio.brownie.network.extension.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Response

class OfflineInterceptor constructor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isNetworkAvailable(context)) {
            Log.d("OFFLINE_INT", "Apply Cache Control directive")
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$MAX_STALE")
                .removeHeader("Pragma")
                .build()
        } else {
            Log.d("OFFLINE_INT", "Network Avaliable")
        }
        return chain.proceed(request)
    }

    companion object {

        // Offline cache available for 30 days
        const val MAX_STALE = 60 * 60 * 24 * 30
    }
}