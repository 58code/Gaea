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
package com.bj58.spat.gaea.server.deploy.filemonitor;

import java.io.File;

/**
 * A class for describe a file
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class FileInfo {
	
	private long lastModifyTime;
	
	private long fileSize;
	
	private String filePath;
	
	private String fileName;
	
	private boolean exists;
	
	
	
	public FileInfo(){
		
	}
	
	public FileInfo(File f) throws Exception{
		if(f != null) {
			this.setFileSize(f.length());
			this.setLastModifyTime(f.lastModified());
			this.setFilePath(f.getCanonicalPath());
			this.setFileName(f.getName());
		} else {
			throw new Exception("File is null");
		}
	}
	

	public void setLastModifyTime(long lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public long getLastModifyTime() {
		return lastModifyTime;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public boolean isExists() {
		return exists;
	}
}