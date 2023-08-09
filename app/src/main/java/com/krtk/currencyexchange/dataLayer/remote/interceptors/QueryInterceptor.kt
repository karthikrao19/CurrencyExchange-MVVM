package com.krtk.currencyexchange.dataLayer.remote.interceptors

import okhttp3.Interceptor
import javax.inject.Inject

class QueryInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val url = request.url.newBuilder()
          //  .addQueryParameter("app_id", BuildConfig.TMDB_API_KEY)
            .build()
        return chain.proceed(request.newBuilder().url(url).build())
    }


}