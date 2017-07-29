package com.zhengdianfang.dazhongbao.models.api

import com.zhengdianfang.dazhongbao.views.StartupData
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by dfgzheng on 26/07/2017.
 */
interface TestApi {
    companion object {
        val HOST = "http://10.38.87.87:11360"
    }

    @GET("/category_hierarchies")
    fun testApi(): Observable<String>
}