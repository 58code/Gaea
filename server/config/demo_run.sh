#!/bin/sh

DIR=`dirname "$0"`
DIR=`cd "$bin"; pwd`

export VM_XMS=2g
export VM_XMX=2g
export VM_XMN=512m
./startup.sh demo $DIR/demo_config.xml $DIR/gaea_log4j.xml
