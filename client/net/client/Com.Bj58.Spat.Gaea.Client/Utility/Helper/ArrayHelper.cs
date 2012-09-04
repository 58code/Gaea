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

namespace Com.Bj58.Spat.Gaea.Client.Utility.Helper
{
    internal class ArrayHelper
    {
        /// <summary>
        /// Compare two arrays are equal
        /// </summary>
        /// <typeparam name="T">array type</typeparam>
        /// <param name="array1">array1</param>
        /// <param name="array2">array2</param>
        /// <returns>equal return true else return false.</returns>
        public static bool Equals<T>(T[] array1 ,T[] array2)
        {
            if (array1 == array2)
                return true;
            if (array1.Length != array2.Length)
                return false;
            for (int i = 0; i < array1.Length; i++)
            {
                if (!array1[i].Equals(array2[i]))
                    return false;
            }
            return true;
        }
        public static bool Equals<T>(T[] array1, int offset, T[] array2)
        {
            if (array1 == array2 && offset == 0)
                return true;
            if (array1.Length - offset < array2.Length)
                return false;
            for (int i = 0; i < array2.Length; i++)
            {
                if (!array1[i + offset].Equals(array2[i]))
                    return false;
            }
            return true;
        }
        /// <summary>
        /// Left array
        /// </summary>
        /// <typeparam name="T">array type</typeparam>
        /// <param name="array">array</param>
        /// <param name="count">move length</param>
        public static void LeftMove<T>(T[] array, int count)
        {
            for (int i = 0; i < array.Length; i++)
            {
                int target = i - count;
                if (target < 0)
                    continue;
                array[target] = array[i];
                array[i] = default(T);
            }
        }
    }
}
