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
 * java convert : convert Object type  to  specific type
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class JavaConvert implements IConvert {
	
	@Override
	public String convertToString(Object obj) {
		if(obj == null) {
			return "";
		}
		return obj.toString();
	}
	
	@Override
	public int convertToint(Object obj) {
		return new Integer(obj.toString()).intValue();
	}
	
	@Override
	public Integer convertToInteger(Object obj) {
		return new Integer(obj.toString());
	}
	
	@Override
	public long convertTolong(Object obj) {
		return new Long(obj.toString()).longValue();
	}
	
	@Override
	public Long convertToLong(Object obj) {
		return new Long(obj.toString());
	}

	@Override
	public short convertToshort(Object obj) {
		return new Short(obj.toString()).shortValue();
	}
	
	@Override
	public Short convertToShort(Object obj) {
		return new Short(obj.toString());
	}

	@Override
	public float convertTofloat(Object obj) {
		return new Float(obj.toString()).floatValue();
	}
	
	@Override
	public Float convertToFloat(Object obj) {
		return new Float(obj.toString());
	}

	@Override
	public boolean convertToboolean(Object obj) {
		return new Boolean(obj.toString()).booleanValue();
	}
	
	@Override
	public Boolean convertToBoolean(Object obj) {
		return new Boolean(obj.toString());
	}
	
	@Override
	public double convertTodouble(Object obj) {
		return new Double(obj.toString()).doubleValue();
	}
	
	@Override
	public Double convertToDouble(Object obj) {
		return new Double(obj.toString());
	}
	
	@Override
	public byte convertTobyte(Object obj) {
		return new Byte(obj.toString()).byteValue();
	}
	
	@Override
	public Byte convertToByte(Object obj) {
		return new Byte(obj.toString());
	}
	
	@Override
	public char convertTochar(Object obj) {
		String str = obj.toString();
		if(str.length() > 1) {
			str = str.replaceFirst("\"", "");
		}
		if(!str.equals(null) && !str.equals("")){
			return str.charAt(0);
		}
		return '\0';
	}
	
	@Override
	public Character convertToCharacter(Object obj) {
		String str = obj.toString();
		if(str.length() > 1) {
			str = str.replaceFirst("\"", "");
		}
		if(!str.equals(null) && !str.equals("")){
			return new Character(str.charAt(0));
		}
		return new Character('\0');
	}
	
	@Override
	public Object convertToT(Object obj, Class<?> clazz) throws Exception {
		return obj;
	}

	@Override
	public Object convertToT(Object obj, Class<?> containClass, Class<?> itemClass) throws Exception {
		return obj;
	}

}