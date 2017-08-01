package com.zhengdianfang.dazhongbao.models.api

import android.content.Context
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 30/07/2017.
 */
object API {
   val appClient  = Retrofit.Builder()
            .baseUrl(TestApi.HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

   fun  mockApiResponse(context: Context?, jsonRawId: Int): Observable<String?> {
       return Observable.just(FileUtils().readRawFile(context, jsonRawId))
               .delay(2, TimeUnit.SECONDS)

   }
}
