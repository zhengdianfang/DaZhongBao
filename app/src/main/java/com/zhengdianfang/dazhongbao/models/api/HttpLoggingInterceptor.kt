package com.zhengdianfang.dazhongbao.models.api

import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.internal.Util


/**
 * Created by dfgzheng on 18/08/2017.
 */
class HttpLoggingInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()
        val requestBody = request?.body()
        val requestStartMessage = "--> ${request?.method()}  ${request?.url()}"
        Logger.d(requestStartMessage)
        Logger.d("Content-type: ${requestBody?.contentType()}  content-length : ${requestBody?.contentLength()}")

        var response = chain?.proceed(request)
        val responseString = response?.body()?.string()
        val newResponse = response?.newBuilder()?.body(ResponseBody.create(response?.body()?.contentType(), responseString?.toByteArray(Util.UTF_8)))?.build()
        Logger.json(responseString)
        response = newResponse
        return response!!
    }
}