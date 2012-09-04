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
using System.Collections;
using System.Collections.Generic;
using System.Security.Cryptography;

namespace Com.Bj58.Spat.Gaea.Client.Secure
{
    class RSACoderHelper
    {
        /*
         * RSA加密(c#、java互通)
         * @param modulus 系数
         * @param exponent 指数
         * @param data 原文
         * @return 密文byte[]
         */
        public static byte[] RSAEncrypt(byte[] data, byte[] modulus, byte[] exponent)
        {
            RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
            RSAParameters rsaparameter = new RSAParameters();
            rsaparameter.Modulus = modulus;
            rsaparameter.Exponent = exponent;
            rsa.ImportParameters(rsaparameter);
            return rsa.Encrypt(data, false);
        }

        /*
         * RSA加密(c#、java互通)
         * @param modulus 系数
         * @param exponent 指数
         * @param data 原文
         * @return 密文
         */
        public static string RSAEncrypt(string data, string modulus, string exponent)
        {
            return Convert.ToBase64String(RSAEncrypt(Encoding.UTF8.GetBytes(data), Convert.FromBase64String(modulus), Convert.FromBase64String(exponent)));
        }

        /*
         * 解密(c#、java互通)
         * @param data 密文
         * @param hashtable RSA公钥私钥集合
         * @return 原文
         */
        public static string RSADecrypt(String data, Hashtable hashtable)
        {
            return Encoding.UTF8.GetString(RSADecrypt(Convert.FromBase64String(data), hashtable));
        }

        /*
         * 解密(c#、java互通)
         * java端通过c#传送的公钥进行加密生成的密文进行解密
         * @param data原文
         * @param hashtable RSA公钥私钥集合
         * @return 原文
         */
        public static byte[] RSADecrypt(byte[] data, Hashtable hashtable)
        {
            RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
            RSAParameters rp = new RSAParameters();
            rp.Modulus = Convert.FromBase64String((string)hashtable["modulus"]);
            rp.Exponent = Convert.FromBase64String((string)hashtable["exponent"]);
            rp.P = Convert.FromBase64String((string)hashtable["p"]);
            rp.Q = Convert.FromBase64String((string)hashtable["q"]);
            rp.DP = Convert.FromBase64String((string)hashtable["dp"]);
            rp.DQ = Convert.FromBase64String((string)hashtable["dq"]);
            rp.InverseQ = Convert.FromBase64String((string)hashtable["inverseq"]);
            rp.D = Convert.FromBase64String((string)hashtable["d"]);
            rsa.ImportParameters(rp);
            return rsa.Decrypt(data, false);
        }

        /*
         * 构造RSA公钥私钥集合(c#、java互通)
         */
        public static Hashtable RSAInitKey()
        {
            Hashtable ht = new Hashtable();
            RSACryptoServiceProvider RSA = new RSACryptoServiceProvider();
            RSAParameters RSAKeyInfo = RSA.ExportParameters(true);
            try
            {
                ht.Add("publickey", Convert.ToBase64String(RSA.ExportCspBlob(false)));
                ht.Add("privatekey", Convert.ToBase64String(RSA.ExportCspBlob(true)));
                ht.Add("d", Convert.ToBase64String(RSAKeyInfo.D));
                ht.Add("dp", Convert.ToBase64String(RSAKeyInfo.DP));
                ht.Add("dq", Convert.ToBase64String(RSAKeyInfo.DQ));
                ht.Add("p", Convert.ToBase64String(RSAKeyInfo.P));
                ht.Add("q", Convert.ToBase64String(RSAKeyInfo.Q));
                ht.Add("inverseq", Convert.ToBase64String(RSAKeyInfo.InverseQ));
                ht.Add("modulus", Convert.ToBase64String(RSAKeyInfo.Modulus));
            }
            catch
            {
                Console.WriteLine("rsa - Hashtable-An element with key already exists");
            }
            return ht;
        }

        /*
        * RSA私钥解密(c#)
        * @param data 原文
        * @param privateKey 密钥
        * @return 原文
        */
        public static string RSADecrypt(string data, string privateKey)
        {
            return Encoding.UTF8.GetString(RSADecrypt(Convert.FromBase64String(data), Convert.FromBase64String(privateKey)));
        }

        /*
        * RSA私钥解密(c#)
        * @param data 原文byte[]
        * @param privateKey 密钥byte[]
        * @return 原文
        */
        public static byte[] RSADecrypt(byte[] data, byte[] privateKey)
        {
            try
            {
                RSACryptoServiceProvider RSA = new RSACryptoServiceProvider();
                RSA.ImportCspBlob(privateKey);
                return RSA.Decrypt(data, false);
            }
            catch (CryptographicException e)
            {
                Console.WriteLine(e.ToString());
                return null;
            }
        }

        /*
         * RSA公钥加密(c#)
         * @param data 原文
         * @param publicKey 密钥
         * @return 密文
         */
        public static string RSAEncrypt(string data, string publicKey)
        {
            return Convert.ToBase64String(RSAEncrypt(Encoding.UTF8.GetBytes(data), Convert.FromBase64String(publicKey)));
        }

        /*
        * RSA公钥加密(c#)
        * @param data 原文byte[]
        * @param publicKey 密钥byte[]
        * @return 密文
        */
        public static byte[] RSAEncrypt(byte[] data, byte[] publicKey)
        {
            try
            {
                RSACryptoServiceProvider RSA = new RSACryptoServiceProvider();
                RSA.ImportCspBlob(publicKey);
                return RSA.Encrypt(data, false);
            }
            catch (CryptographicException e)
            {
                Console.WriteLine(e.Message);
                return null;
            }
        }
    }
}
