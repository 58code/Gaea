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
package com.bj58.spat.gaea.serializer.component.helper;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * FileHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class FileHelper {

    public static List<File> getFiles(String dir, String... extension) {
        File f = new File(dir);
        if (!f.isDirectory()) {
            return null;
        }

        List<File> fileList = new ArrayList<File>();
        getFiles(f, fileList, extension);

        return fileList;
    }

    private static void getFiles(File f, List<File> fileList, String... extension) {
        File[] files = f.listFiles();
        if(files==null){
            return;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getFiles(files[i], fileList, extension);
            } else if (files[i].isFile()) {
                String fileName = files[i].getName().toLowerCase();
                boolean isAdd = false;
                if (extension != null) {
                    for (String ext : extension) {
                        if (fileName.lastIndexOf(ext) == fileName.length() - ext.length()) {
                            isAdd = true;
                            break;
                        }
                    }
                }
                if (isAdd) {
                    fileList.add(files[i]);
                }
            }
        }
    }
}
