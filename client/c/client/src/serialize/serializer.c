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
#include "serializer.h"
#include "byteHelper.h"
#include "structHelper.h"
#include "strHelper.h"
#include "serializeList.h"
#include "../protocol/SdpStruct.h"
#include <time.h>
#include <stddef.h>
#include <stdlib.h>
#include <objc/hash.h>

int WriteRef(serializerInfo *info, const void *obj) {
	if (obj == NULL) {
		return 0;
	}
	int i = 0, flg = 0;
	int dataLen = info->refObjArray->byteLength / sizeof(void *);
	void *data = info->refObjArray->data;
	for (i = 0; i < dataLen; ++i) {
		if (*(void**) data == obj) {
			flg = 1;
			break;
		}
		data += sizeof(void *);
	}
	byteArrayPutData(info->outArray, &flg, 1);
	if (flg == 0) {
		info->hashcode++;
		byteArrayPutData(info->outArray, &(info->hashcode), 4);
		byteArrayPutData(info->refObjArray, &obj, sizeof(void*));
	} else {
		int hashcode = 1001 + i;
		byteArrayPutData(info->outArray, &hashcode, 4);
	}
	return flg;
}
void enumSerializer(int typeId, serializerInfo *info, const void *obj) {
	if (WriteRef(info, obj)) {
		return;
	}
	int len = strlen(obj);
	byteArrayPutData(info->outArray, &len, 4);
	byteArrayPutData(info->outArray, obj, len);
}
void nullSerializer(int typeId, serializerInfo *info, const void *obj) {
	writeInt32(0, info->outArray);
}
void charSerializer(int typeId, serializerInfo *info, const void *obj) {
	byteArrayPutData(info->outArray, obj, 1);
}
void stringSerializer(int typeId, serializerInfo *info, const void *obj) {
	if (WriteRef(info, obj)) {
		return;
	}
	int len = strlen(obj);
	byteArrayPutData(info->outArray, &len, 4);
	byteArrayPutData(info->outArray, obj, len);
}
void shortIntSerializer(int typeId, serializerInfo *info, const void *obj) {
	byteArrayPutData(info->outArray, obj, 2);
}
void intSerializer(int typeId, serializerInfo *info, const void *obj) {
	byteArrayPutData(info->outArray, obj, 4);
}
void floatSerializer(int typeId, serializerInfo *info, const void *obj) {
	byteArrayPutData(info->outArray, obj, 4);
}
void doubleSerializer(int typeId, serializerInfo *info, const void *obj) {
	byteArrayPutData(info->outArray, obj, 8);
}
void timeSerializer(int typeId, serializerInfo *info, const void *obj) {
	const time_t *t = obj;
	long long l = *t * 1000;
	l += 8 * 60 * 60 * 1000;
	byteArrayPutData(info->outArray, &l, 8);

}
void longLongSerializer(int typeId, serializerInfo *info, const void *obj) {
	byteArrayPutData(info->outArray, obj, 8);
}
void arraySerializer(int typeId, serializerInfo *info, const void *obj) {
	const array *ay = obj;
	if (ay->objectLength == 0) {
		return nullSerializer(typeId, info, obj);
	}
	byteArrayPutData(info->outArray, &(ay->typeId), 4);
	if (WriteRef(info, obj)) {
		return;
	}
	byteArrayPutData(info->outArray, &(ay->objectLength), 4);
	int i = 0;
	char isP = IsPrimitive(ay->typeId);
	void *data = ay->data;
	serializer sr;
	if (ay->isPointe == 1) {
		for (i = 0; i < ay->objectLength; ++i) {
			sr = getSerializer(ay->typeId, *(void**) data);
			if (sr != nullSerializer && !isP) {
				byteArrayPutData(info->outArray, &ay->typeId, 4);
			}
			sr(ay->typeId, info, *(void**) data);
			data += sizeof(void *);
		}
	} else {
		int typeSize = getObjectSize(ay->typeId);
		for (i = 0; i < ay->objectLength; ++i) {
			sr = getSerializer(ay->typeId, data);
			if (sr != nullSerializer && !isP) {
				byteArrayPutData(info->outArray, &ay->typeId, 4);
			}
			sr(ay->typeId, info, data);
			data += typeSize;
		}
	}

}
void listSerializer(int typeId, serializerInfo *info, const void *obj) {
	struct serialize_list *list = (void*) obj;
	int listLen = list_length(list);
	if (listLen == 0) {
		return nullSerializer(typeId, info, obj);
	}
	byteArrayPutData(info->outArray, &typeId, 4);
	if (WriteRef(info, obj)) {
		return;
	}
	byteArrayPutData(info->outArray, &listLen, 4);
	int i = 0;
	struct serialize_list *cell;
	serializer sr;
	for (i = 0; i < listLen; ++i) {
		cell = list_nth(i, list);
		sr = getSerializer(cell->typeId, cell->head);
		byteArrayPutData(info->outArray, &cell->typeId, 4);
		sr(cell->typeId, info, cell->head);
	}
}
void mapSerializer(int typeId, serializerInfo *info, const void *obj) {
	cache_ptr hashmap = (cache_ptr) obj;
	if (obj == NULL || hashmap->used == 0) {
		return nullSerializer(typeId, info, obj);
	}
	int dataSize = hashmap->used;
	byteArrayPutData(info->outArray, &typeId, 4);
	if (WriteRef(info, obj)) {
		return;
	}
	byteArrayPutData(info->outArray, &dataSize, 4);
	node_ptr node = objc_hash_next(hashmap, NULL);
	hashmapEntry *entry;
	serializer sr;
	while (node) {
		entry = (hashmapEntry*) node->key;
		sr = getSerializer(entry->typeId, entry->data);
		byteArrayPutData(info->outArray, &entry->typeId, 4);
		sr(entry->typeId, info, entry->data);
		entry = node->value;
		sr = getSerializer(entry->typeId, entry->data);
		if (sr != nullSerializer) {
			byteArrayPutData(info->outArray, &entry->typeId, 4);
		}
		sr(entry->typeId, info, entry->data);
		node = objc_hash_next(hashmap, node);
	}
}
void structSerializer(int typeId, serializerInfo *info, const void *obj) {
	const void *newObj = obj;
	array *structInfo = objc_hash_value_for_key(structInfoMap, &typeId);
	if (structInfo == NULL || structInfo->byteLength == 0) {
		nullSerializer(typeId, info, newObj);
		return;
	}
	int i = 0;
	structFieldInfo *sfi = structInfo->data;
	int fieldLen = structInfo->byteLength / sizeof(structFieldInfo);
	writeInt32(typeId, info->outArray);
	if (sfi[1].typeId != SERIALIZE_ENUM_N) {
		if (WriteRef(info, newObj)) {
			return;
		}
	}
	++sfi;
	serializer sr;
	int t = 0;
	for (i = 1; i < fieldLen; ++i) {
		newObj = obj + sfi->offset;
		if (sfi->isPointe == 1) {
			if (sfi->typeId == SERIALIZE_VOID_N) {
				t = *(int*) (obj + sfi->offset + sizeof(void*));
				sr = getSerializer(t, *(void**) newObj);
			} else {
				t = sfi->typeId;
				sr = getSerializer(sfi->typeId, *(void**) newObj);
			}
			if (sr != nullSerializer && sr != enumSerializer) {
				byteArrayPutData(info->outArray, &t, 4);
			}
			sr(t, info, *(void**) newObj);
		} else {
			sr = getSerializer(sfi->typeId, newObj);
			if (sr != nullSerializer && sr != enumSerializer) {
				byteArrayPutData(info->outArray, &sfi->typeId, 4);
			}
			sr(sfi->typeId, info, newObj);
		}
		++sfi;
	}
}

serializer getSerializer(int typeId, const void *obj) {
	if (obj == NULL) {
		return nullSerializer;
	}
	serializer sr;
	switch (typeId) {
	case SERIALIZE_CHAR_N:
	case SERIALIZE_BOOL_N:
		sr = charSerializer;
		break;
	case SERIALIZE_SHORT_INT_N:
		sr = shortIntSerializer;
		break;
	case SERIALIZE_INT_N:
		sr = intSerializer;
		break;
	case SERIALIZE_TIME_N:
		sr = timeSerializer;
		break;
	case SERIALIZE_FLOAT_N:
		sr = floatSerializer;
		break;
	case SERIALIZE_DOUBLE_N:
		sr = doubleSerializer;
		break;
	case SERIALIZE_LONG_LONG_N:
		sr = longLongSerializer;
		break;
	case SERIALIZE_ARRAY_N:
		sr = arraySerializer;
		break;
	case SERIALIZE_LIST_N:
		sr = listSerializer;
		break;
	case SERIALIZE_MAP_N:
		sr = mapSerializer;
		break;
	case SERIALIZE_STRING_N:
		sr = stringSerializer;
		break;
	case SERIALIZE_ENUM_N:
		sr = enumSerializer;
		break;
	default: {
		sr = structSerializer;
		break;
	}
	}

	return sr;
}
char* Serialize(char *type, const void *obj, const int *retDataLen) {
	array retArray;
	retArray.byteLength = 0;
	retArray.data = NULL;
	int typeId = GetTypeId(type);
	array refObjArray = { byteLength:0, data:NULL};
	serializerInfo info = { outArray:&retArray, hashcode:1000, refObjArray:&refObjArray };
	if (obj == NULL) {
		writeInt32(0, &retArray);
		memcpy((void*) retDataLen, &(retArray.byteLength), sizeof(retArray.byteLength));
		return retArray.data;
	}
	serializer sr = getSerializer(typeId, obj);
	sr(typeId, &info, obj);
	if (info.refObjArray->data) {
		free(info.refObjArray->data);
	}
	memcpy((void*) retDataLen, &(retArray.byteLength), sizeof(retArray.byteLength));
	return retArray.data;
}
