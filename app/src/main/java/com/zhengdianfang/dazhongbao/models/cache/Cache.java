package com.zhengdianfang.dazhongbao.models.cache;

/**
 * Created by zheng on 16/6/15.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * An interface for a cache keyed by a String with a byte array as data.
 */
public interface Cache {
    /**
     * Retrieves an entry from the cache.
     * @param key Cache key
     * *
     * @return An [Entry] or null in the event of a cache miss
     */
     Entry get(String key);

    /**
     * Adds or replaces an entry to the cache.
     * @param key Cache key
     * *
     * @param entry Data to store and metadata for cache coherency, TTL, etc.
     */
     void put(String key, Entry entry);

    /**
     * Performs any potentially long-running actions needed to initialize the cache;
     * will be called from a worker thread.
     */
    void initialize();

    /**
     * Invalidates an entry in the cache.
     * @param key Cache key
     * *
     * @param fullExpire True to fully expire the entry, false to soft expire
     */
    void invalidate(String key, boolean fullExpire);

    /**
     * Removes an entry from the cache.
     * @param key Cache key
     */
    void remove(String key);

    /**
     * Empties the cache.
     */
    void clear();

    /**
     * Data and metadata for an entry returned by the cache.
     */
    public class Entry {
        /** The data returned from cache.  */
        public byte[] data = null;

        /** ETag for cache coherency.  */
        public String etag = "";

        /** Date of this response as reported by the server.  */
        public  long serverDate = 0;

        /** TTL for this record.  */
        public long ttl = 0;

        /** Soft TTL for this record.  */
        public long softTtl = 0;

        public Object obj = null;

        /** Immutable response headers as received from server; must be non-null.  */
        public  Map<String, String> responseHeaders = new HashMap<>();

        /** True if the entry is expired.  */
        public  boolean isExpired;

        /** True if a refresh is needed from the original data source.  */
        public boolean refreshNeeded() {
            return this.softTtl < System.currentTimeMillis();
        }

        public boolean isExpired() {
            return this.ttl != 0L && this.ttl < System.currentTimeMillis();
        }
    }


}
