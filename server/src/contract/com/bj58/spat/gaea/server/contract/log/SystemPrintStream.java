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
package com.bj58.spat.gaea.server.contract.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * redirect System.out & System.err into log4j file
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SystemPrintStream extends PrintStream {

	private ILog log = LogFactory.getLogger(SystemPrintStream.class);

	private static PrintStream outInstance = new SystemPrintStream(System.out);
	private static PrintStream errInstance = new SystemPrintStream(System.err);

	private SystemPrintStream(OutputStream out) {
		super(out);
	}

	public static void redirectToLog4j() {
		System.setOut(outInstance);
		System.setErr(errInstance);
	}

	public PrintStream append(char c) {
		// Ignore
		return this;
	}

	public PrintStream append(CharSequence csq, int start, int end) {
		// Ignore
		return this;
	}

	public PrintStream append(CharSequence csq) {
		// Ignore
		return this;
	}

	public boolean checkError() {
		// Ignore
		return false;
	}

	protected void clearError() {
		// Ignore
	}

	public void close() {
		// Ignore
	}

	public void flush() {
		// Ignore
	}

	public PrintStream format(Locale l, String format, Object... args) {
		// Ignore
		return this;
	}

	public PrintStream format(String format, Object... args) {
		// Ignore
		return this;
	}

	public void print(boolean b) {
		println(b);
	}

	public void print(char c) {
		println(c);
	}

	public void print(char[] s) {
		println(s);
	}

	public void print(double d) {
		println(d);
	}

	public void print(float f) {
		println(f);
	}

	public void print(int i) {
		println(i);
	}

	public void print(long l) {
		println(l);
	}

	public void print(Object obj) {
		println(obj);
	}

	public void print(String s) {
		println(s);
	}

	public PrintStream printf(Locale l, String format, Object... args) {
		// Ignore
		return this;
	}

	public PrintStream printf(String format, Object... args) {
		// Ignore
		return this;
	}

	public void println() {
		// Ignore
	}

	public void println(boolean x) {
		if(this == errInstance) {
			log.error(String.valueOf(x));
		} else {
			log.info(String.valueOf(x));
		}
	}

	public void println(char x) {
		if(this == errInstance) {
			log.error(String.valueOf(x));
		} else {
			log.info(String.valueOf(x));
		}
	}

	public void println(char[] x) {
		if(this == errInstance) {
			log.error(x == null ? null : new String(x));
		} else {
			log.info(x == null ? null : new String(x));
		}
	}

	public void println(double x) {
		if(this == errInstance) {
			log.error(String.valueOf(x));
		} else {
			log.info(String.valueOf(x));
		}
	}

	public void println(float x) {
		if(this == errInstance) {
			log.error(String.valueOf(x));
		} else {
			log.info(String.valueOf(x));
		}
	}

	public void println(int x) {
		if(this == errInstance) {
			log.error(String.valueOf(x));
		} else {
			log.info(String.valueOf(x));
		}
	}

	public void println(long x) {
		if(this == errInstance) {
			log.error(String.valueOf(x));
		} else {
			log.info(String.valueOf(x));
		}
	}

	public void println(Object x) {
//		log.info("------------------------");
//		StackTraceElement[] stack = (new Throwable()).getStackTrace();
//		for(StackTraceElement ste : stack) {
//			log.info(ste.getClassName());
//		}
//		log.info("+----------------------+");
		
		if(this == errInstance) {
			log.error(String.valueOf(x));
		} else {
			log.info(String.valueOf(x));
		}
	}

	public void println(String x) {
		if(this == errInstance) {
			log.error(x);
		} else {
			log.info(x);
		}
	}

	protected void setError() {
		// Ignore
	}

	public void write(byte[] buf, int off, int len) {
		// Ignore
	}

	public void write(int b) {
		// Ignore
	}

	public void write(byte[] b) throws IOException {
		// Ignore
	}
}