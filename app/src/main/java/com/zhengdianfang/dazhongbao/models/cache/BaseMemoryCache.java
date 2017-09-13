package com.zhengdianfang.dazhongbao.models.cache;

import android.content.Context;
import android.support.v4.util.LruCache;

import java.util.Calendar;

/**
 * Created by zheng on 16/6/15.
 */

public class BaseMemoryCache {

    private LruCache<String, Entry> memoryCache = new LruCache(50);

    public static BaseMemoryCache fromCtx(Context context) {
        return new BaseMemoryCache();
    }

    public class Entry{

        public long ttl;
        public Object obj;

        Entry(Object obj, long ttl){
            this.ttl = ttl;
            this.obj = obj;
        }

        public boolean isExprid(){
            Calendar c = Calendar.getInstance();
            return c.getTimeInMillis() >= ttl;
        }
    }


    public void add(String key, Object obj, int second){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, second);
        Entry entry = new Entry(obj, c.getTimeInMillis());
        memoryCache.put(key, entry);
    }
    public void add(String key, Object obj){
        Calendar c = Calendar.getInstance();
        Entry entry = new Entry(obj, c.getTimeInMillis());
        memoryCache.put(key, entry);
    }

    public Entry get(String key){
        return memoryCache.get(key);
    }

    public void clear(){
        memoryCache.evictAll();
    }
}
