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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.util.FileHelper;

/**
 * A class for check file is change
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class FileMonitor {
	
	/**
	 * log
	 */
	private static ILog logger = LogFactory.getLogger(FileMonitor.class);
	
	/**
	 * listener
	 */
    private static List<IListener> listenerList = new ArrayList<IListener>();
    
    /**
     * files which will check
     */
    private static List<FileInfo> fileList = new ArrayList<FileInfo>();
    
    /**
     * check interval, default:1000ms
     */
    private static long interval = 1000;

    private static Timer timer = null;
 
    private static NotifyCount notifyCount = null; 
    
    private static FileMonitor monitor;
    
    private static Object lockHelper = new Object();
    
    
    /**
     * get FileMonitor instance
     * @return
     */
    public static FileMonitor getInstance(){
    	if(monitor == null) {
    		synchronized (lockHelper) {
    			if(monitor == null) {
    				monitor = new FileMonitor();
    			}
			}
    	}
    	return monitor;
    }
    
    
    /**
     * begin timer
     */
    public void start() {
    	timer = new Timer();
        timer.schedule(new CheckTask(), 1000, interval);
    }
    
    
    /**
     * add listener when file changed will notify it
     * @param listener
     */
    public void addListener(IListener listener) {
        listenerList.add(listener);
    }
    
    /**
     * add monitor file
     * @param fileInfo
     */
    public void addMonitorFile(FileInfo fileInfo) {
    	fileList.add(fileInfo);
    }
    
    /**
     * add files which in dir to monitor
     * @param dir
     * @throws Exception
     */
    public void addMonitorFile(String dir) throws Exception {
    	List<File> fList = FileHelper.getFiles(dir, "jar", "ear", "war", "xml");
    	for(File file : fList) {
    		logger.info("add monitor file:" + file.getAbsolutePath());
    		fileList.add(new FileInfo(file));
    	}
    }
   
    /**
     * get all monitor file
     * @return
     */
    public List<FileInfo> getMonitoredFiles() {
        return fileList;
    }
    
    
    /**
     * timer task class
     *
     */
    private class CheckTask extends java.util.TimerTask {
		@Override
		public void run() {
			try {
				if(fileList != null) {
					boolean isChange = false;
					for(FileInfo fInfo : fileList) {
						File f = new File(fInfo.getFilePath());
						fInfo.setExists(f.exists());
						if(f.exists()){
							long length = f.length();
							long modifyTime = f.lastModified();
							
							if(modifyTime != fInfo.getLastModifyTime() ||
									length != fInfo.getFileSize()){
								
								logger.info("file change:" + f.getAbsolutePath());
								logger.info("newLength:" + length);
								logger.info("oldLength:" + fInfo.getFileSize());
								logger.info("newModifyTime:" + modifyTime);
								logger.info("oldModifyTime:" + fInfo.getLastModifyTime());
								
								fInfo.setFileSize(length);
								fInfo.setLastModifyTime(modifyTime);
								
								isChange = true;
								
								if(FileMonitor.notifyCount == NotifyCount.EachChangeFile) {
									fireFilesChangedEvent(fInfo);
								}
							}
						} else {
							isChange = true;
							
							if(FileMonitor.notifyCount == NotifyCount.EachChangeFile) {
								fireFilesChangedEvent(fInfo);
							}
						}
	        		}

					if(FileMonitor.notifyCount == NotifyCount.Once && isChange) {
						fireFilesChangedEvent(null);
					}
				}
			} catch (Exception e) {
				logger.error("check file error", e);
			}
		}
	}
    

    /**
     * notify all listener
     * @param fi
     */
    private void fireFilesChangedEvent(FileInfo fi) {
    	logger.info("listenerList size : "+listenerList.size());
        for( int i = 0; i < listenerList.size(); i++ ) {
            listenerList.get(i).fileChanged(fi);
        }
    }


	public long getInterval() {
		return FileMonitor.interval;
	}


	public void setInterval(long interval) {
		FileMonitor.interval = interval;
	}


	public void setNotifyCount(NotifyCount notifyCount) {
		FileMonitor.notifyCount = notifyCount;
	}


	public NotifyCount getNotifyCount() {
		return FileMonitor.notifyCount;
	}
}