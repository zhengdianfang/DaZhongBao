package com.zhengdianfang.dazhongbao.models.login

import com.fasterxml.jackson.core.type.TypeReference
import com.hyphenate.util.FileUtils
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.CException
import com.zhengdianfang.dazhongbao.models.api.Result
import com.zhengdianfang.dazhongbao.models.api.UserApi
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import com.zhengdianfang.dazhongbao.models.mock.mockUserProducts
import com.zhengdianfang.dazhongbao.models.product.Product
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 08/08/2017.
 */
class UserRepository(private var MOCK: Boolean = Constants.MOCK) {
    fun modifyPassword(password: String, token: String): Observable<String> {
        return API.appClient.create(UserApi::class.java).modifyPassword(password , "", "", token)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }

    }

    fun setPassword(password: String, token: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).modifyPassword(password , "", "", token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }

    }

    fun findPassword(password: String, verifyCode: String,  phoneNumber: String): Observable<String> {
        return API.appClient.create(UserApi::class.java).modifyPassword(password , phoneNumber, verifyCode, "")
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }

    }

    fun verifySmsCode(phoneNumber: String, ac: Int, verifyCode: String): Observable<Boolean> {
        return API.appClient.create(UserApi::class.java).verifySmsCode(phoneNumber, ac, verifyCode)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map true
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }

    fun uploadBusinessLicenceCard(token: String, contactName: String, companyName: String, file: File): Observable<User> {
        val requestBody = RequestBody.create(MediaType.parse(FileUtils.getMIMEType(file)), file)
        val body =  MultipartBody.Part.createFormData("fileName", file.name, requestBody)
        return API.appClient.create(UserApi::class.java)
                .uploadBusinessLicenceCard(token, contactName, companyName, body)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun uploadBusinessCard(token: String, content: String, file: File): Observable<User> {
        val requestBody = RequestBody.create(MediaType.parse(FileUtils.getMIMEType(file)), file)
        val body =  MultipartBody.Part.createFormData("fileName", file.name, requestBody)
        return API.appClient.create(UserApi::class.java)
                .uploadBusinessCard(token, content, body)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun uploadContactCard(token: String, cardFrontFile: File, cardBackEndFile: File): Observable<User> {
        val requestBody1 = RequestBody.create(MediaType.parse(FileUtils.getMIMEType(cardFrontFile)), cardFrontFile)
        val requestBody2 = RequestBody.create(MediaType.parse(FileUtils.getMIMEType(cardBackEndFile)), cardBackEndFile)
        val body1 =  MultipartBody.Part.createFormData("fileName", cardFrontFile.name, requestBody1)
        val body2 =  MultipartBody.Part.createFormData("fileName2", cardBackEndFile.name, requestBody2)
        return API.appClient.create(UserApi::class.java)
                .uploadContactCard(token, body1, body2)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun modifyPhoneNumber(token: String, phoneNumber: String, verifyCode: String): Observable<User> {
        return API.appClient.create(UserApi::class.java).modifyPhoneNumber(token, phoneNumber, verifyCode)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun fetchIndexCount(token: String): Observable<IntArray>{
        return API.appClient.create(UserApi::class.java).fetchIndexCount(token)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        val data = json.get("data")
                        return@map intArrayOf(data.get("DealCount").asInt(), data.get("ProductCount").asInt(), data.get("MessageCount").asInt())
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }

    fun fetchUserPushedProduct(token: String): Observable<Result<MutableList<Product>>> {
        if (MOCK){
            return Observable.just(mockUserProducts).delay(2, TimeUnit.SECONDS).map { list -> Result(list) }
        }
        return API.appClient.create(UserApi::class.java).fetchUserPushedProduct(token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){})}
                .map { list ->  Result(list)}
    }

    fun fetchUserAttentionProducts(token: String): Observable<Result<MutableList<Product>>>{
        if (MOCK){
            return Observable.just(mockUserProducts).delay(2, TimeUnit.SECONDS).map { list -> Result(list) }
        }
        return API.appClient.create(UserApi::class.java).fetchUserAttentionProducts(token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){})}
                .map { list ->  Result(list)}
    }

    fun fetchUserAuctionProducts(token: String): Observable<Result<MutableList<Product>>>{
        if (MOCK){
            return Observable.just(mockUserProducts).delay(2, TimeUnit.SECONDS).map { list -> Result(list) }
        }
        return API.appClient.create(UserApi::class.java).fetchUserAuctionProducts(token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){})}
                .map { list ->  Result(list)}
    }

    fun fetchUserInfo(token: String): Observable<Pair<User, UserCount>>{
        return API.appClient.create(UserApi::class.java).fetchUserInfo(token)
                .map {json->
                    val user = API.objectMapper.readValue(json.get("data").toString(), User::class.java)
                    val userCount = API.objectMapper.readValue(json.get("usercount").toString(), UserCount::class.java)
                    Pair(user, userCount)
                }
    }

    fun fetchDepositProducts(token: String): Observable<MutableList<Product>>{
        return API.appClient.create(UserApi::class.java).fetchUserDepsitProducts(token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){})}

    }

    fun uploadUserAvatar(token: String, avatarFile: File): Observable<User>{
        val requestBody = RequestBody.create(MediaType.parse(FileUtils.getMIMEType(avatarFile)), avatarFile)
        val body =  MultipartBody.Part.createFormData("avatar", avatarFile.name, requestBody)
        return API.appClient.create(UserApi::class.java)
                .uploadUserAvatar(token, body)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun fetchIMUserInfo(token: String, userIds: String): Observable<MutableList<User>>{
        return API.appClient.create(UserApi::class.java).fetchIMUserInfo(token, userIds)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<User>>(data, object : TypeReference<MutableList<User>>(){})}
    }

    fun updateUmengId(token: String, umengId: String): Observable<String> {
        return API.appClient.create(UserApi::class.java).updateUMengToken(token, umengId)
                .map {response -> API.parseResponse(response) }
    }
}
