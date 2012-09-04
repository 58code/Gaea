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
package com.bj58.spat.gaea.client.utility.logger;

import com.bj58.spat.gaea.client.GaeaConst;

/**
 * FileLog
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class FileLog implements ILog {

    public static org.apache.commons.logging.Log logger = null;

    public FileLog(Class<?> cls) {
        logger = org.apache.commons.logging.LogFactory.getLog(cls);
    }

    @Override
    public void fine(String message) {
        logger.info(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void config(String message) {
        logger.info(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void info(String message) {
        logger.info(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void warning(String message) {
        logger.warn(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void debug(String message) {
        logger.debug(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void debug(String message, Throwable t) {
        logger.debug(GaeaConst.VERSION_FLAG + message, t);
    }

    @Override
    public void warn(String message) {
        logger.warn(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void error(String message) {
        logger.error(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void error(String message, Throwable t) {
        logger.error(GaeaConst.VERSION_FLAG + message, t);
    }

    @Override
    public void error(Throwable e) {
        logger.error(e);
    }

    @Override
    public void fatal(String message) {
        logger.fatal(GaeaConst.VERSION_FLAG + message);
    }

    @Override
    public void fatal(String message, Throwable t) {
        logger.fatal(GaeaConst.VERSION_FLAG + message, t);
    }

    @Override
    public void info(String message, Throwable t) {
        logger.info(GaeaConst.VERSION_FLAG + message, t);
    }

    @Override
    public void warn(String message, Throwable t) {
        logger.warn(GaeaConst.VERSION_FLAG + message, t);
    }
}
