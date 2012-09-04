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
package com.bj58.spat.gaea.server.secure;

/**
 * BaseNCodec
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class BaseNCodec{
    public static final int MIME_CHUNK_SIZE = 76;
    public static final int PEM_CHUNK_SIZE = 64;
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    protected static final int MASK_8BITS = 0xff;
    protected static final byte PAD_DEFAULT = '='; 
    protected final byte PAD = PAD_DEFAULT; 
    private final int unencodedBlockSize;
    private final int encodedBlockSize;
    protected final int lineLength; 
    private final int chunkSeparatorLength;
    protected byte[] buffer;
    protected int pos;
    private int readPos;
    protected boolean eof;
    protected int currentLinePos;
    protected int modulus;
    
    protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength){
        this.unencodedBlockSize = unencodedBlockSize;
        this.encodedBlockSize = encodedBlockSize;
        this.lineLength = (lineLength > 0  && chunkSeparatorLength > 0) ? (lineLength / encodedBlockSize) * encodedBlockSize : 0;
        this.chunkSeparatorLength = chunkSeparatorLength;
    }

    boolean hasData() {
        return this.buffer != null;
    }

    int available() {
        return buffer != null ? pos - readPos : 0;
    }

    protected int getDefaultBufferSize() {
        return DEFAULT_BUFFER_SIZE;
    }

    private void resizeBuffer() {
        if (buffer == null) {
            buffer = new byte[getDefaultBufferSize()];
            pos = 0;
            readPos = 0;
        } else {
            byte[] b = new byte[buffer.length * DEFAULT_BUFFER_RESIZE_FACTOR];
            System.arraycopy(buffer, 0, b, 0, buffer.length);
            buffer = b;
        }
    }

    protected void ensureBufferSize(int size){
        if ((buffer == null) || (buffer.length < pos + size)){
            resizeBuffer();
        }
    }

    int readResults(byte[] b, int bPos, int bAvail) {
        if (buffer != null) {
            int len = Math.min(available(), bAvail);
            System.arraycopy(buffer, readPos, b, bPos, len);
            readPos += len;
            if (readPos >= pos) {
                buffer = null;
            }
            return len;
        }
        return eof ? -1 : 0;
    }

    protected static boolean isWhiteSpace(byte byteToCheck) {
        switch (byteToCheck) {
            case ' ' :
            case '\n' :
            case '\r' :
            case '\t' :
                return true;
            default :
                return false;
        }
    }
    
    private void reset() {
        buffer = null;
        pos = 0;
        readPos = 0;
        currentLinePos = 0;
        modulus = 0;
        eof = false;
    }

    public Object encode(Object pObject) throws Exception {
        if (!(pObject instanceof byte[])) {
            throw new Exception("Parameter supplied to Base-N encode is not a byte[]");
        }
        return encode((byte[]) pObject);
    }

    public String encodeToString(byte[] pArray) {
        return StringUtils.newStringUtf8(encode(pArray));
    }
    
    public Object decode(Object pObject) throws Exception {        
        if (pObject instanceof byte[]) {
            return decode((byte[]) pObject);
        } else if (pObject instanceof String) {
            return decode((String) pObject);
        } else {
            throw new Exception("Parameter supplied to Base-N decode is not a byte[] or a String");
        }
    }

    public byte[] decode(String pArray) {
        return decode(StringUtils.getBytesUtf8(pArray));
    }

    public byte[] decode(byte[] pArray) {
        reset();
        if (pArray == null || pArray.length == 0) {
            return pArray;
        }
        decode(pArray, 0, pArray.length);
        decode(pArray, 0, -1);
        byte[] result = new byte[pos];
        readResults(result, 0, result.length);
        return result;
    }

    public byte[] encode(byte[] pArray) {
        reset();        
        if (pArray == null || pArray.length == 0) {
            return pArray;
        }
        encode(pArray, 0, pArray.length);
        encode(pArray, 0, -1); 
        byte[] buf = new byte[pos - readPos];
        readResults(buf, 0, buf.length);
        return buf;
    }
    
    public String encodeAsString(byte[] pArray){
        return StringUtils.newStringUtf8(encode(pArray));
    }

    abstract void encode(byte[] pArray, int i, int length);

    abstract void decode(byte[] pArray, int i, int length);
    
    protected abstract boolean isInAlphabet(byte value);
       
    public boolean isInAlphabet(byte[] arrayOctet, boolean allowWSPad) {
        for (int i = 0; i < arrayOctet.length; i++) {
            if (!isInAlphabet(arrayOctet[i]) &&
                    (!allowWSPad || (arrayOctet[i] != PAD) && !isWhiteSpace(arrayOctet[i]))) {
                return false;
            }
        }
        return true;
    }

    public boolean isInAlphabet(String basen) {
        return isInAlphabet(StringUtils.getBytesUtf8(basen), true);
    }

    protected boolean containsAlphabetOrPad(byte[] arrayOctet) {
        if (arrayOctet == null) {
            return false;
        }
        for (int i = 0; i < arrayOctet.length; i++) {
            if (PAD == arrayOctet[i] || isInAlphabet(arrayOctet[i])) {
                return true;
            }
        }
        return false;
    }

    public long getEncodedLength(byte[] pArray) {
        long len = ((pArray.length + unencodedBlockSize-1)  / unencodedBlockSize) * (long) encodedBlockSize;
        if (lineLength > 0) {
            len += ((len + lineLength-1) / lineLength) * chunkSeparatorLength;
        }
        return len;
    }
}