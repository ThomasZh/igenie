package com.oct.ga.comm.domain;

import java.io.Serializable;

public interface JsonBean
		extends Serializable
{
	public String encode();

	public JsonBean decode(String json);
}
