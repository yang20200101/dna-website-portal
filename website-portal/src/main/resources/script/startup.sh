#!/bin/sh
JRE_HOME="/usr/local/jdk/jre/bin"
JAVA_OPTS="-server -Xms1024m -Xmx4096m -XX:MaxNewSize=1024m"
APP_NAME="website-portal-0.0.1-SNAPSHOT"
JAR_NAME="${APP_NAME}.jar"
pid=""
this_dir="$( cd "$( dirname "$0"  )" && pwd )"
#使用说明，用来提示输入参数nmo
usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status]"
    exit 1
}

#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
    return 1
  else
    return 0
  fi
}

#启动方法
start(){
  if [ ! -d "$this_dir/logs" ];then
     mkdir -p "$this_dir/logs"
  else
     echo "文件夹已存在"
  fi

  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `

  if [ -z "${pid}" ]; then
    nohup $JRE_HOME/java $JAVA_OPTS -jar $this_dir/$JAR_NAME  --server.port=8008 --server.servlet.context-path=/web-portal >$this_dir/logs/website-portal.log 2>&1 &
    echo ">>> start $JAR_NAME successed PID=$! <<<" 
  else
    echo ">>> $JAR_NAME is already running PID=${pid} <<<"  
  fi

  }

#停止方法
stop(){
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  if [ -z "${pid}" ]; then
    echo ">>> $JAR_NAME  is not running <<<"
  else
    echo ">>> $JAR_NAME 2 PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2
    echo ">>> $JAR_NAME process stopped <<<"
  fi  

}

#输出运行状态
status(){
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  if [ -z "${pid}" ]; then
    echo ">>> ${JAR_NAME} is running PID is ${pid} <<<"
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi
}

#重启
restart(){
  stop
  start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0
