package com.zhengdianfang.dazhongbao.models.mock

import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Deal
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 31/07/2017.
 */

val mockUser = User(id = "1",
        token = "123123",
        realname = "刘凯",
        avatar =  "http://img3.imgtn.bdimg.com/it/u=1882374716,3145400819&fm=26&gp=0.jpg",
        companyName = "thoughtworks",
        companyAddress = "北京市东城区西直们",
        email = "dfghzheng@thoughtworks.com",
        phonenumber = "18511177911",
        position = "manager",
        level = 0,
        type = 1,
        integrity = 1,
        businessCard = "http://",
        contactCard = "http://",
        contactCard2 = "http://",
        contactCardStatus = "0",
        businessCardStatus = "0",
        businessLicence = "http://",
        businessLicenceStatus = "0"
)


val mockSmsCode = "122444"


val mockDealList = mutableListOf(Deal("刘凯", price = 23.01, count = 100000),
        Deal("王凯", price = 24.01, count = 400000),
        Deal("黄凯", price = 25.51, count = 200000))

val mockBid = Bid(1, realname = "刘凯", price = 23.11, count = 10000, productId = 9, ctime = System.currentTimeMillis(), sharesName = "九江银行", sharesCode = "000048", highest = 55.00, status = 1)
val mockBidList = mutableListOf(
        Bid(1, realname = "刘凯", price = 23.11, count = 10000, productId = 9, ctime = System.currentTimeMillis(), sharesName = "九江银行", sharesCode = "000048", highest = 45.00, status = 1),
//        Bid(2, realname = "王凯", price = 25.00, count = 20000, productId = 9, ctime = System.currentTimeMillis(), sharesName = "九江银行", sharesCode = "000048"),
        Bid(3, realname = "黄凯", price = 11.00, count = 40000, productId = 9, ctime = System.currentTimeMillis(), sharesName = "九江银行", sharesCode = "000048", highest = 34.11, status = 1)
)

//val mockProduct = Product(id = 9, sharesCode = "000048", basicUnitPrice = 21.40, soldCount = 2110000, limitTime = 6, industry = "金融业",
//check_status = 5, sharesName = "九江银行", description = "test", bond = 0, bond_status = 0, yestodayClosePrice = 0.0, attention = 1, bidcount = 0,
//companyName = "" ,contact = mockUser, lastUnitPrice = 19.01, deal = mockDealList, nowUnitPrice = 23.19, startDateTime = 0, endDateTime = System.currentTimeMillis() + 1000 * 24 * 3600)
val mockProduct = Product(id = 10, sharesCode = "000049", basicUnitPrice = 21.40, soldCount = 2110000, limitTime = 6, industry = "金融业",
                check_status = 5, sharesName = "山东银行", description = "test", bond = "0", bond_status = 2, yestodayClosePrice = 0.0, attention = 0, bidcount = 0,
                companyName = "" , lastUnitPrice = 19.01, deal = mockDealList, nowUnitPrice = 23.19, startDateTime = System.currentTimeMillis() + 1000 * 24 * 3600, endDateTime = 0
, mybids = mockBidList, csm_user = null, contact = null)

val mockUserProducts = mutableListOf(
        Product(id = 5, sharesCode = "002739", basicUnitPrice = 23.09, soldCount = 3000000, limitTime = 6, industry = "汽车",
                check_status = 3, sharesName = "万达电影", description = "adadfsdfsdf", bond = "0", bond_status = 0, yestodayClosePrice = 0.0, attention = 0, bidcount = 0,
                companyName = "" , lastUnitPrice = 29.01, deal = mockDealList, nowUnitPrice = 22.19, startDateTime = 0, endDateTime = 0, mybids = mutableListOf(), csm_user = null, contact = null)
)

