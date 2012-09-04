package com.bj58.enterprise.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class EntityBase implements Serializable{
	public abstract String getCacheKey();
	  
	public static String generateCacheKey(Integer id, Class<?> clazz) {
		if (id == null || clazz==null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(clazz.getName());
		sb.append("_");
		sb.append(id);
		return sb.toString();
	}
	
	public static String generateCacheKey(Long id, Class<?> clazz) {
		if (id == null || clazz==null) {
			return null;
		}
		StringBuilder sb = new StringBuilder(clazz.getName());
		sb.append("_");
		sb.append(id);
		return sb.toString();
	}
}
