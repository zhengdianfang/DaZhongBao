package com.zhengdianfang.dazhongbao.models.cache;

/**
 * Created by zheng on 16/6/15.
 */

public interface IRxCache {

     void addInCache(String key, byte[] data);
     Cache.Entry getFromCache(String key);
}
