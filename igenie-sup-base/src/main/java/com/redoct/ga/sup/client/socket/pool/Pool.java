package com.redoct.ga.sup.client.socket.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public abstract class Pool<T>
{
	private final GenericObjectPool internalPool;

	public Pool(final GenericObjectPoolConfig poolConfig, BasePooledObjectFactory factory)
	{
		this.internalPool = new GenericObjectPool(factory, poolConfig);
	}

	@SuppressWarnings("unchecked")
	public T getResource()
	{
		try {
			return (T) internalPool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException("Could not get a resource from the pool", e);
		}
	}

	public void returnResourceObject(final Object resource)
	{
		try {
			internalPool.returnObject(resource);
		} catch (Exception e) {
			throw new RuntimeException("Could not return the resource to the pool", e);
		}
	}

	public void returnBrokenResource(final T resource)
	{
		returnBrokenResourceObject(resource);
	}

	public void returnResource(final T resource)
	{
		returnResourceObject(resource);
	}

	protected void returnBrokenResourceObject(final Object resource)
	{
		try {
			internalPool.invalidateObject(resource);
		} catch (Exception e) {
			throw new RuntimeException("Could not return the resource to the pool", e);
		}
	}

	public void destroy()
	{
		try {
			internalPool.close();
		} catch (Exception e) {
			throw new RuntimeException("Could not destroy the pool", e);
		}
	}
}
