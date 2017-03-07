package com.example.api.service;

import com.example.api.common.exception.BusinessException;
import com.example.api.entities.CacheCustomData;

public interface CacheCustomDataService extends GenericService<CacheCustomData>{

    /**
     * Returns the object with id=key. Null if not exists.<br>
     * <b> Important: implementations should delete the object if it is expired.<b>
     * @param key
     * @return
     */
    <E> E get(String key, Class<E> type);
    	
    /**
     * Saves the data into the storage, expiring in given seconds
     * @param data
     * @param expirySeconds
     * @throws BusinessException 
     */
    void save(String key, Object data, int expirySeconds) throws BusinessException;
}
