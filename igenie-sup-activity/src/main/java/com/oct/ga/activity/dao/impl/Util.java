package com.oct.ga.activity.dao.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Util {
	private static final Charset UTF_8 = Charset.forName("utf-8");
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidStr = uuid.toString();
		return uuidStr.replace("-", "");
	}

	public static int currentTimeSeconds() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static Blob toBlob(String s) throws SerialException, SQLException {
		Blob blob = null;
		if (s != null && s.length() > 0) {
			byte[] bytes = s.getBytes(UTF_8);
			blob = new SerialBlob(bytes);
		}
		return blob;
	}

	public static String toString(Blob blob) throws SQLException {
		String s = null;
		if (blob != null && blob.length() > 0) {
			byte[] bytes = blob.getBytes(1, (int) blob.length());
			s = new String(bytes, UTF_8);
		}
		return s;
	}

	public static String toJson(Object object) {
		try {
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new JsonException(e);
		}
	}

	public static <E> List<E> jsonToList(String json, Class<E> elementType) {
		CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
		try {
			return OBJECT_MAPPER.readValue(json, collectionType);
		} catch (IOException e) {
			throw new JsonException(e);
		}
	}
}
