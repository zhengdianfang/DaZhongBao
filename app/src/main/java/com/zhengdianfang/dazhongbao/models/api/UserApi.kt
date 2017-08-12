package com.zhengdianfang.dazhongbao.models.api

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Created by dfgzheng on 26/07/2017.
 */
interface UserApi {

    @POST("users/login")
    fun loginByPhone(@Query("username") username: String,
                     @Query("password") password: String,
                     @Query("ver") ver: String,
                     @Query("deviceid") deviceId: String,
                     @Query("platform") platform: String = "Android"): Observable<JsonNode>

    @POST("users/sendvno")
    fun getSmsVerifyCode(@Query("phonenumber") phoneNumber: String,
                     @Query("ac") type: Int): Observable<JsonNode>

    @POST("users/registered")
    fun register(@Query("phonenumber") phoneNumber: String,
                 @Query("verifyCode") verifyCode: String,
                 @Query("recommendPerson") recommendPerson: String): Observable<JsonNode>

    @POST("users/resetpwd")
    fun modifyPassword(@Query("password") password: String,
                       @Query("phonenumber") phoneNumber: String,
                       @Query("verifyCode") verifyCode: String,
                       @Query("token") token: String): Observable<JsonNode>

    @POST("users/checkvno")
    fun verifySmsCode(@Query("phonenumber") phoneNumber: String,
                      @Query("ac") type: Int,
                      @Query("verifyCode") verifyCode: String
    ): Observable<JsonNode>

    @Multipart
    @POST("users/businessLicense")
    fun uploadBusinessLicenceCard(@Part("token") token: String,
                                  @Part("contactName") contactName: String,
                                  @Part("companyName") companyName: String,
                                  @Part file: MultipartBody.Part): Observable<JsonNode>

    @Multipart
    @POST("users/businessCard")
    fun uploadBusinessCard(@Part("token") token: String,
                                  @Part("content") content: String,
                                  @Part file: MultipartBody.Part): Observable<JsonNode>

    @Multipart
    @POST("users/constactCard")
    fun uploadContactCard(@Part("token") token: String,
                           @Part file1: MultipartBody.Part,
                          @Part file2: MultipartBody.Part): Observable<JsonNode>

    @POST("users/resetPhonenumber")
    fun modifyPhoneNumber(@Query("token") token: String , @Query("phonenumber") phoneNumber: String,
                 @Query("verifyCode") verifyCode: String): Observable<JsonNode>
}