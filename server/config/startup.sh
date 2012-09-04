#!/bin/sh

### ====================================================================== ###
##                                                                          ##
##                       Gaea server bootstrap script                        ##
##                                                                          ##
### ====================================================================== ###

source /etc/profile

USAGE="Usage: startup.sh <service-name> [<other-gaea-config>]"
SYSTEM_PROPERTY=""

# if no args specified, show usage
if [ $# -lt 1 ]; then
  echo $USAGE
  exit 1
fi

cd $(dirname "$0")

# get arguments
SERVICE_NAME=$1
OTHER_GAEA_CONFIG=""
for((i=2; i<=$#; i++)); do
  OTHER_GAEA_CONFIG=$OTHER_GAEA_CONFIG" "${!i}
done

#if this service is not shutwodn Please shutdown
SERVICE_DIR=`dirname "$0"`
SERVICE_DIR=`cd "$bin"; pwd`
PID_FILE="$SERVICE_DIR"/../tmp/pid/"$SERVICE_NAME"

if [ -e $PID_FILE ]; then
  PID_INFO=`cat $PID_FILE`
  SERVICE_PID=`ps -ef | grep -v grep | grep $PID_INFO | sed -n  '1P' | awk '{print $2}'` 
  echo "ps -ef | grep -v grep | grep $PID_INFO | sed -n  '1P' | awk '{print $2}'"
  echo $SERVICE_PID
  if [ $SERVICE_PID ]; then
    echo "startup fail! Please close the service after to restart!" 
    echo `date` +"[$SERVICE_NAME] is running" >> ../log/monitor.log
    exit 1 
  else 
    echo "This service will startup!"
    echo `date` +"[$SERVICE_NAME] is starting" >> ../log/monitor.log
  fi
fi


# check tools.jar
if [ ! -f "$JAVA_HOME"/lib/tools.jar ]; then
  echo "Can't find tools.jar in JAVA_HOME"
  echo "Need a JDK to run javac"
  exit 1
fi


# check service is run
javacount=`ps -ef|grep java|grep "sn:$SERVICE_NAME" |wc -l`
#echo "javacount:"$javacount
if [ $javacount -ge 1 ] ; then
  echo "warning: has a [$SERVICE_NAME] is running, please check......................................"
  exit 1
fi



# get path
DIR="$GAEA_HOME"bin
if [ "$DIR" = "bin" ]; then
  echo "no GAEA_HOME path"
  DIR=`dirname "$0"`
  DIR=`cd "$bin"; pwd`
fi
PROGNAME=`basename $0`
ROOT_PATH="$DIR"/..
DEPLOY_PATH="$ROOT_PATH"/service/deploy
PID_PATH="$ROOT_PATH"/tmp/pid



# java opts
if [ "$VM_XMS" = "" ]; then
  VM_XMS=256m
fi

if [ "$VM_XMX" = "" ]; then
  VM_XMX=256m
fi

if [ "$VM_XMN" = "" ]; then
  VM_XMN=128m
fi

JAVA_OPTS="-Xms$VM_XMS -Xmx$VM_XMX -Xmn$VM_XMN -Xss1024K -XX:PermSize=256m -XX:MaxPermSize=512m -XX:ParallelGCThreads=20 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:SurvivorRatio=65536 -XX:MaxTenuringThreshold=0 -XX:CMSInitiatingOccupancyFraction=80"



# class path
CLASS_PATH=.:"$JAVA_HOME"/lib/tools.jar

for jar in $ROOT_PATH/lib/*.jar; do
  CLASS_PATH=$CLASS_PATH:$jar
done



# main class
MAIN_CLASS=com.bj58.spat.gaea.server.bootstrap.Main


java $JAVA_OPTS -classpath $CLASS_PATH -Duser.dir=$DIR $SYSTEM_PROPERTY $MAIN_CLASS $OTHER_GAEA_CONFIG -Dgaea.service.name=$SERVICE_NAME &

echo pid:$!

echo $! > "$PID_PATH"/"$SERVICE_NAME"

