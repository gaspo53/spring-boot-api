package com.example.api.service.impl;

import org.springframework.stereotype.Service;

import com.example.api.common.exception.BusinessException;
import com.example.api.common.utils.LogHelper;
import com.example.api.entities.CacheCustomData;
import com.example.api.service.CacheCustomDataService;

@Service
public class AbstractCacheCustomDataService extends GenericServiceImpl<CacheCustomData>
											implements CacheCustomDataService{
    
    @SuppressWarnings("unchecked")
	@Override
    public <E> E get(String key, Class<E> type) {
        E content = null;
        
        CacheCustomData cacheData = this.getById(key);
        boolean expired = (cacheData == null);
        
        if (!expired){
        	content = (E) cacheData.getValue();
        }
        
        return content;
    }

    @Override
    public void save(String key, Object data, int expirySeconds) throws BusinessException {
    	CacheCustomData cacheData;
		try {
			cacheData = new CacheCustomData();
			cacheData.setKey(key);
			cacheData.setValue(data);
			cacheData.expiresAfter(expirySeconds);
			
			this.save(cacheData);
		} catch (Exception e) {
			LogHelper.error(this,e);
			throw new BusinessException(e);
		}
    }
}
