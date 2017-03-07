package com.example.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.example.api.entities.CacheCustomData;

@Repository
public class CacheCustomDataMongoDaoImpl extends GenericMongoDAOImpl<CacheCustomData>{

	public CacheCustomDataMongoDaoImpl() {
		super(CacheCustomData.class);
	}
}
