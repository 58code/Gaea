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
//----------------------------------------------------------------------
//<Copyright company="58.com">
//      Team:SPAT
//      Blog:http://blog.58.com/spat/  
//      Website:http://www.58.com
//</Copyright>
//-----------------------------------------------------------------------
using System;
using System.IO;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
using System.Collections.Generic;

namespace Com.Bj58.Spat.Gaea.Client.Secure
{
    class DESCoderHelper
    {
        /*
          * 加密(c#、java互通)
          * 用"流"的方式进行加密
          * data 原文
          * key 密钥(length must be 8 bytes long)
          * return 密文
          */
        public static string DESEncryptByStream(string data, string key)
        {
            byte[] byKey = Encoding.UTF8.GetBytes(key);
            byte[] byIV = Encoding.UTF8.GetBytes(key);

            String retStr = "";
            StreamWriter sw = null;
            MemoryStream ms = null;
            CryptoStream cst = null;
            try
            {
                DESCryptoServiceProvider cryptoProvider = new DESCryptoServiceProvider();
                ms = new MemoryStream();
                cst = new CryptoStream(ms, cryptoProvider.CreateEncryptor(byKey, byIV), CryptoStreamMode.Write);

                sw = new StreamWriter(cst);
                sw.Write(data);

                sw.Flush();//清除文本流
                cst.FlushFinalBlock();//清除加密流中的内容

                retStr = Convert.ToBase64String(ms.GetBuffer(), 0, (int)ms.Length);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            finally
            {
                if (sw != null)
                {
                    sw.Close();
                }
                if (ms != null)
                {
                    ms.Close();
                }
                if (cst != null)
                {
                    cst.Close();
                }
            }
            return retStr;
        }

        /*
         * 解密(c#、java互通)
         * 用"流"的方式进行解密
         * 解密过程为：密文转化为byte数组->内存流->解密流->原始文字
         * data 密文
         * key  密钥 (length must be 8 bytes long)
         * return 原文
         */
        public static string DESDeccryptByStream(string data, string key)
        {
            byte[] byKey = Encoding.UTF8.GetBytes(key);
            byte[] byIV = Encoding.UTF8.GetBytes(key);

            string retStr = "";
            byte[] byEnc;
            MemoryStream ms = null;
            CryptoStream cst = null;
            StreamReader sr = null;
            try
            {
                byEnc = Convert.FromBase64String(data);//将密文转换成数组
                ms = new MemoryStream(byEnc);
                //des加密算法执行类
                DESCryptoServiceProvider cryptoProvider = new DESCryptoServiceProvider();
                //解密流接到内存流上
                cst = new CryptoStream(ms, cryptoProvider.CreateDecryptor(byKey, byIV), CryptoStreamMode.Read);
                sr = new StreamReader(cst);//文本读取流
                retStr = sr.ReadToEnd();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            finally
            {
                if (ms != null)
                {
                    ms.Close();
                }
                if (cst != null)
                {
                    cst.Close();
                }
                if (sr != null)
                {
                    sr.Close();
                }
            }
            return retStr;
        }

        /* 
        * DES加密(c#、java互通)
        * data 原文
        * key 密钥(length must be 8 bytes long)
        * return 密文(注:密文为通过base64加密后的字符串)
        */
        public static string DESEncrypt(string data, string key)
        {
            DES des = new DESCryptoServiceProvider();
            des.Key = Encoding.UTF8.GetBytes(key);
            des.IV = Encoding.UTF8.GetBytes(key);
            byte[] bytes = Encoding.UTF8.GetBytes(data);
            return Convert.ToBase64String(des.CreateEncryptor().TransformFinalBlock(bytes, 0, bytes.Length));
        }

        /* 
        * DES 解密(c#、java互通)
        * data 密文
        * key 密钥(length must be 8 bytes long)
        * return 原文
        */
        public static string DESDeccrypt(string data, string key)
        {
            DES des = new DESCryptoServiceProvider();
            des.Key = Encoding.UTF8.GetBytes(key);
            des.IV = Encoding.UTF8.GetBytes(key);
            byte[] bytes = Convert.FromBase64String(data);
            return Encoding.UTF8.GetString(des.CreateDecryptor().TransformFinalBlock(bytes, 0, bytes.Length));
        }
    }
}
