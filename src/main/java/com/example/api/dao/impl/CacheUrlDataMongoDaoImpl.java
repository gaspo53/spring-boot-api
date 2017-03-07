package com.example.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.example.api.entities.CacheUrlData;

@Repository
public class CacheUrlDataMongoDaoImpl extends GenericMongoDAOImpl<CacheUrlData>{

	public CacheUrlDataMongoDaoImpl() {
		super(CacheUrlData.class);
	}
	
}
