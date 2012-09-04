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

/**
 * ILog
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public interface ILog {

   /**
    * Logging a fine message
    * @param message the message to log
    */
   void fine(String message);

   /**
    * Logging a config message
    * @param message the message to log
    */
   void config(String message);

   /**
    * Logging an info message
    * @param message the message to log
    */
   void info(String message);


   /**
    * Logging a warning message
    * @param message the message to log
    */
   void warning(String message);


 
   /**
    * Logging a debug message
    * @param message the message to log
    */
   void debug(String message);

   /**
    * Logging a debug message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void debug(String message, Throwable t);

   /**
    * Logging an info message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void info(String message, Throwable t);

   /**
    * Logging a warning message
    * @param message the message to log
    */
   void warn(String message);

   /**
    * Logging a warning message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void warn(String message, Throwable t);

   /**
    * Logging an error message
    * @param message the message to log
    */
   void error(String message);

   /**
    * Logging an error message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void error(String message, Throwable t);
   
   /**
    * Logging an error
    * @param e
    */
   void error(Throwable e);

   /**
    * Logging a fatal message
    * @param message the message to log
    */
   void fatal(String message);

   /**
    * Logging a fatal message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void fatal(String message, Throwable t);
}