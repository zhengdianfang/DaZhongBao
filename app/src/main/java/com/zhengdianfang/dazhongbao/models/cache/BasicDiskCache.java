package com.zhengdianfang.dazhongbao.models.cache;

import android.content.Context;

import java.io.File;

/**
 * Created by zheng on 16/6/15.
 */

public class BasicDiskCache implements IRxCache{

    private BaseDiskCache diskCache;
    private static final  long  REASONABLE_DISK_SIZE = 5 * 1024 * 1024;
    private static final int REASONABLE_MEM_ENTRIES = 50 ;// 50 entries

    public static BasicDiskCache fromCtx(Context context) {
        return new BasicDiskCache(
                new File(context.getCacheDir(), "retrofit_rxcache"),
                REASONABLE_DISK_SIZE);
    }

    public BasicDiskCache(File diskDirectory, long maxDiskSize) {

        diskCache = new BaseDiskCache(diskDirectory, maxDiskSize);
        diskCache.initialize();
    }

    @Override
    public void addInCache(String key, byte[] data) {
        Cache.Entry entry = new Cache.Entry();
        entry.data = data;
        String cacheKey = urlToKey(key);
        diskCache.put(cacheKey, entry);
    }

    @Override
    public Cache.Entry getFromCache(String key) {
        String cacheKey = urlToKey(key);

        Cache.Entry diskResponse = diskCache.get(cacheKey);
        if (diskResponse != null) {
            return diskResponse;
        } else {
            return null;
        }
    }

    private String urlToKey(String key) {
        return MD5.getMD5(key);
    }

    public void clearDiskCache() {
        diskCache.clear();
    }
}
