package com.oct.ga.message.dao.mongodb;

import org.springframework.data.mongodb.MongoDbFactory;

public class SimpleMongoTemplate
{
	public MongoDbFactory getMongoDbFactory()
	{
		return mongoDbFactory;
	}

	public void setMongoDbFactory(MongoDbFactory mongoDbFactory)
	{
		this.mongoDbFactory = mongoDbFactory;
	}

	private MongoDbFactory mongoDbFactory;

}