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
package com.bj58.spat.gaea.serializer.serializer;

import com.bj58.spat.gaea.serializer.component.GaeaInStream;
import com.bj58.spat.gaea.serializer.component.GaeaOutStream;
import com.bj58.spat.gaea.serializer.component.exception.OutOfRangeException;
import com.bj58.spat.gaea.serializer.component.helper.ByteHelper;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;

import java.sql.Timestamp;
import java.util.Date;

/**
 * DateTimeSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class DateTimeSerializer extends SerializerBase {

	private long TimeZone = 8 * 60 * 60 * 1000;

	@Override
	public void WriteObject(Object obj, GaeaOutStream outStream) throws Exception {
		byte[] buffer = ConvertToBinary((Date) obj);
		outStream.write(buffer);
	}

	@Override
	public Object ReadObject(GaeaInStream inStream, Class defType) throws Exception {
		byte[] buffer = new byte[8];
		inStream.SafeRead(buffer);
		Date date = GetDateTime(buffer);
		if (defType == java.sql.Timestamp.class) {
			return new Timestamp(date.getTime());
		} else if (defType == java.sql.Date.class) {
			return new java.sql.Date(date.getTime());
		} else if (defType == java.sql.Time.class) {
			return new java.sql.Time(date.getTime());
		}
		return date;
	}

	private byte[] ConvertToBinary(Date dt) {
		Date dt2 = new Date();
		// set 1970-1-1 00:00:00
		dt2.setTime(0);
		long rel = dt.getTime() - dt2.getTime();
		return ByteHelper.GetBytesFromInt64(rel + TimeZone);
	}

	private Date GetDateTime(byte[] buffer) throws OutOfRangeException {
		long rel = ByteHelper.ToInt64(buffer);
		Date dt = new Date(rel - TimeZone);
		return dt;
	}
}
