package com.oct.ga.activity.dao.impl;

import org.springframework.dao.DataAccessException;

public class JsonException extends DataAccessException {
	private static final long serialVersionUID = 5225125330604549055L;

	public JsonException(Throwable cause) {
		super("Json error", cause);
	}

}
