#!/bin/sh
####
# 这是一个springBoot服务启停脚本
#  @author sweeter
####

# 引入环境变量
#JDK环境配置 环境变量用于解决执行java命令出现java: command not found的问题
export JAVA_HOME=/usr/jdk1.8.0_171
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

JAVA_OPTS=" -Dfile.encoding=UTF-8 -server -d64 -Xmx2g -Xms2g -Djava.security.egd=file:/dev/./urandom  -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDateStamps -XX:+UseGCLogFileRotation -XX:GCLogFileSize=10M -XX:NumberOfGCLogFiles=10 -Xloggc:./logs/gc.log -XX:+UseG1GC -XX:+PrintTenuringDistribution -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/dump.hprof"

# 全局变量
name=sls
work_dir=`pwd`"/"
pid_file=$work_dir$name.pid

# 启动进程
start_(){
        nohup java $JAVA_OPTS -jar -Dloader.path=lib  $work_dir'sls.jar' --spring.profiles.active=deploy >> $name.log 2>&1 &
        pid=$!
        echo "Start Java Service = $pid";
        echo $pid > $pid_file
}

# 停止进程
stop_(){
        echo "Stop Java Service";
        kill -15  `cat $pid_file` &> /dev/null
}

case "$1" in
     restart)
        stop_
        sleep 3s
        start_
           ;;

     start)
      start_
           ;;
     stop)
      stop_
           ;;
     *)
        echo "Usage: $work_dir$name.sh {start|stop|restart}"
        exit 1
           ;;

esac
exit 0
