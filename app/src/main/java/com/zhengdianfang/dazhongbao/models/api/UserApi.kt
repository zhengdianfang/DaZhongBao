package com.zhengdianfang.dazhongbao.models.api

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Created by dfgzheng on 26/07/2017.
 */
interface UserApi {

    @FormUrlEncoded
    @POST("users/login")
    fun loginByPhone(@Field("username") username: String,
                     @Field("password") password: String,
                     @Field("ver") ver: String,
                     @Field("deviceid") deviceId: String,
                     @Field("platform") platform: String = "Android"): Observable<JsonNode>

    @FormUrlEncoded
    @POST("users/thridLogin")
    fun loginByThridParty(@Field("openid") openid: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("users/sendvno")
    fun getSmsVerifyCode(@Field("phonenumber") phoneNumber: String,
                     @Field("ac") type: Int): Observable<JsonNode>

    @FormUrlEncoded
    @POST("users/registered")
    fun register(@Field("phonenumber") phoneNumber: String,
                 @Field("verifyCode") verifyCode: String,
                 @Field("recommendPerson") recommendPerson: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("users/thridRegistered")
    fun registerByThrid(@Field("phonenumber") phoneNumber: String,
                 @Field("openid") openid: String,
                 @Field("verifyCode") verifyCode: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("users/resetpwd")
    fun modifyPassword(@Field("password") password: String,
                       @Field("phonenumber") phoneNumber: String,
                       @Field("verifyCode") verifyCode: String,
                       @Field("token") token: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("users/checkvno")
    fun verifySmsCode(@Field("phonenumber") phoneNumber: String,
                      @Field("ac") type: Int,
                      @Field("verifyCode") verifyCode: String
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

    @FormUrlEncoded
    @POST("users/resetPhonenumber")
    fun modifyPhoneNumber(@Field("token") token: String , @Field("phonenumber") phoneNumber: String,
                 @Field("verifyCode") verifyCode: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("index/getIndexCount")
    fun fetchIndexCount(@Field("token") token: String): Observable<JsonNode>


    @FormUrlEncoded
    @POST("products/myPlist")
    fun fetchUserPushedProduct(@Field("token")token: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/myAttentions")
    fun fetchUserAttentionProducts(@Field("token")token: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/myBidProducts")
    fun fetchUserAuctionProducts(@Field("token")token: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/bondProducts")
    fun fetchUserDepsitProducts(@Field("token")token: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("users/getUserInfo")
    fun fetchUserInfo(@Field("token")token: String): Observable<JsonNode>

    @Multipart
    @POST("users/modifyUserInfo")
    fun uploadUserAvatar(@Part("token") token: String, @Part file: MultipartBody.Part): Observable<JsonNode>


    @FormUrlEncoded
    @POST("users/getIMUsers")
    fun fetchIMUserInfo(@Field("token")token: String, @Field("userIds")userIds: String): Observable<JsonNode>


    @FormUrlEncoded
    @POST("users/updateUmengId")
    fun updateUMengToken(@Field("token")token: String, @Field("umengId")umengId: String): Observable<JsonNode>
}