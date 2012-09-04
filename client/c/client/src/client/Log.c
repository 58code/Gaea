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
#include "Log.h"
#include "GaeaConst.h"
static char* logFile = GAEA_LOG_PATH;
void gaeaLog(int level, const char *fmt, ...) {
	int GAEA_VERBOSITY = 0;
	if (level < GAEA_VERBOSITY) {
		return;
	}
	const char *c = ".-*#";
	time_t now = time(NULL);
	va_list ap;
	FILE *fp;
	char buf[64];
	char msg[1024];

	fp = fopen(logFile, "a");
	if (!fp)
	return;

	va_start(ap, fmt);
	vsnprintf(msg, sizeof(msg), fmt, ap);
	va_end(ap);

	strftime(buf, sizeof(buf), "%F %H:%M:%S", localtime(&now));
	fprintf(fp, "[%d] %s %c %s\n", (int) getpid(), buf, c[level], msg);
	fflush(fp);
	fclose(fp);

}
void setLogFilePath(char* pa) {
	logFile = pa;
}
