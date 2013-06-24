#!/bin/sh

## USAGE="Usage: reboot.sh <service-startscript> <time>"
## service-startscript:this script name is servicestartscript name
## example:
## umc_run.sh    (reboot.sh umc_run 10)
## imc_start.sh  (reboot.sh imc_start 10)
### ====================================================================== ###
##                                                                          ##
##                       GAEA server reboot script                        ##
##                                                                          ##
### ====================================================================== ###

USAGE="Usage: reboot.sh <service-name|service-startscript> <time>"

# if no args specified, show usage
if [ $# -ne 2 ]; then
  echo $USAGE
  exit 1
fi

# get arguments
SERVICE_STARTSCRIPT=$1
SERVICE_NAME=$1
CONTROL_TIME=$2

cd $(dirname "$0")

case x$2 in
x[0-9]*)
;;
*)
echo "please input time num"
exit 1
;;
esac 

if [ "$SERVICE_STARTSCRIPT" ]; then
    if [ `expr index "$SERVICE_NAME" "_"` > 0 ];then
	      SERVICE_NAME=$(echo "$SERVICE_STARTSCRIPT"|cut -d "_" -f 1)
        if [ `expr index "$SERVICE_NAME" "."` > 0 ]; then
           SERVICE_NAME=$(echo "$SERVICE_NAME"|cut -d "." -f 1)
        fi
    fi
else 
    echo "this server name is null Please input server name"
    exit 1
fi


DIR=`dirname "$0"`
DIR=`cd "$bin"; pwd`
PID_FILE="$DIR"/../tmp/pid/"$SERVICE_NAME"

if [ ! -e $PID_FILE ]; then
  echo "pid file($PID_FILE) not exits"
  exit 1
fi

cd $(dirname "$0")

#send reboot signal
kill -12 `cat $PID_FILE`
echo "kill -12 `cat $PID_FILE`"

#sleep time 
echo ""$CONTROL_TIME" seconds after restart service"
sleep `expr $CONTROL_TIME`

#shutdown this server
kill -9 `cat $PID_FILE`
echo "kill -9 `cat $PID_FILE`"
rm -rf $PID_FILE


#start this server
STARTSCRIPT_FILE="$DIR"/"$SERVICE_STARTSCRIPT".sh
echo "$STARTSCRIPT_FILE"
if [ ! -e $STARTSCRIPT_FILE ]; then
    echo "defult startup.sh"
    source "$DIR"/startup.sh "$SERVICE_NAME"
else 
    echo "this server start script"
    source "$DIR"/"$SERVICE_STARTSCRIPT".sh
fi

