package com.example.api.service;

import com.example.api.entities.CacheUrlData;

public interface CacheDataService extends GenericService<CacheUrlData>{

    /**
     * Returns the object with id=key. Null if not exists.<br>
     * <b> Important: implementations should delete the object if it is expired.<b>
     * @param key
     * @return
     */
    String get(String key);
}
