package com.example.api.service.impl;

import com.example.api.entities.CacheUrlData;
import com.example.api.service.CacheDataService;

public abstract class AbstractCacheUrlDataService extends GenericServiceImpl<CacheUrlData>
                                               	  implements CacheDataService{
    
    @Override
    public String get(String key) {
        String content = null;
        
        CacheUrlData cacheData = this.getById(key);
        boolean expired = (cacheData == null);
        
        if (!expired){
        	content = cacheData.getValue();
        }
        
        return content;
    }

}
