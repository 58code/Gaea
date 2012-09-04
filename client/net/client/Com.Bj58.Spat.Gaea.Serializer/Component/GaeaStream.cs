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
using System.Linq;
using System.Text;
using System.Collections;
using System.Collections.Generic;

namespace Com.Bj58.Spat.Gaea.Serializer.Component
{
    public sealed class GaeaStream : System.IO.MemoryStream
    {
        public GaeaStream(byte[] buffer)
            : base(buffer)
        { }

        public GaeaStream()
            : base()
        { }

        public GaeaStream(GaeaStream source)
        {
            this.RefPool = source.RefPool;
        }

        private Dictionary<long, object> _RefPool = new Dictionary<long, object>();

        public Dictionary<long, object> RefPool
        {
            get { return _RefPool; }
            set { _RefPool = value; }
        }

        private Encoding _Encoder = Encoding.UTF8;
        public Encoding Encoder
        {
            get { return _Encoder; }
            set { _Encoder = value; }
        }

        public bool WriteRef(object obj)
        {
            if (obj == null)
            {
                WriteByte(1);
                this.WriteInt32(0);
                return true;
            }

            var hashCode = GetHashCode(obj);
            object tem;
            if (_RefPool.TryGetValue(hashCode, out tem))
            {
                WriteByte(1);
                Write(hashCode.GetBytes(), 0, 4);
                return true;
            }
            else
            {
                _RefPool[hashCode] = obj;
                WriteByte(0);
                Write(hashCode.GetBytes(), 0, 4);
                return false;
            }
        }

        public object GetRef(int hashcode)
        {
            if (hashcode == 0)
                return null;
            object obj;
            _RefPool.TryGetValue(hashcode, out obj);
            return obj;
        }

        public void SetRef(int hashcode, object obj)
        {
            _RefPool[hashcode] = obj;
        }
       
#if VERSION1_1
        private int _hashcode = 1000;
        private int CreatHashCode()
        {
            lock (this)
            {
                return (++_hashcode);
            }
        }
        private Dictionary<object, int> _ObjDic = new Dictionary<object, int>();
#endif
        private int GetHashCode(object obj)
        {
#if VERSION1_1
            if (obj == null)
                return 0;
            int hashCode;
            if (!_ObjDic.TryGetValue(obj, out hashCode))
            {
                hashCode = CreatHashCode();
                _ObjDic[obj] = hashCode;
            }
            return hashCode;
#else
            return obj.GetHashCode();
#endif
        }
    }
}
