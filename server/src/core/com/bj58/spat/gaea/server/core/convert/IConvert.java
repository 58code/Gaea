/*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.spat.gaea.server.core.convert;

/**
 * a interface for description convert object to target type
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface IConvert {
	
	public String convertToString(Object obj);
	
	public int convertToint(Object obj);
	
	public Integer convertToInteger(Object obj);
	
	public long convertTolong(Object obj);
	
	public Long convertToLong(Object obj);

	public short convertToshort(Object obj);
	
	public Short convertToShort(Object obj);

	public float convertTofloat(Object obj);
	
	public Float convertToFloat(Object obj);

	public boolean convertToboolean(Object obj);
	
	public Boolean convertToBoolean(Object obj);
	
	public double convertTodouble(Object obj);
	
	public Double convertToDouble(Object obj);
	
	public byte convertTobyte(Object obj);
	
	public Byte convertToByte(Object obj);
	
	public char convertTochar(Object obj);
	
	public Character convertToCharacter(Object obj);
	
	public Object convertToT(Object obj, Class<?> clazz) throws Exception;
	
	public Object convertToT(Object obj, Class<?> containClass, Class<?> itemClass) throws Exception;
}