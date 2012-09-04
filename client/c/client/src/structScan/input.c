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
#include <stdio.h>
#include <stdlib.h>
#include "input.h"
void ReadSourceFile(char *filename) {
	Input.file = fopen(filename, "r");
	if (Input.file == NULL) {
		printf("Can't open file: %s.", filename);
	}

	fseek(Input.file, 0, SEEK_END);
	Input.size = ftell(Input.file);
	Input.base = malloc(Input.size + 1);
	if (Input.base == NULL) {
		printf("The file %s is too big", filename);
		fclose(Input.file);
	}
	fseek(Input.file, 0, SEEK_SET);
	Input.size = fread(Input.base, 1, Input.size, Input.file);
	fclose(Input.file);

	Input.filename = filename;
	Input.base[Input.size] = 255;
	Input.cursor = Input.lineHead = Input.base;
	Input.line = 1;

	return;
}

void CloseSourceFile(void) {

	free(Input.base);

}

