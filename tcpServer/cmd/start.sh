#! /bin/sh
_TCP_SERVER_JAVA_HOME=/usr/lib/jvm/java-17-openjdk-17.0.1.0.12-1.rolling.el7.x86_64
#_TCP_SERVER_JAVA_HOME=/Users/songpc/Library/Java/JavaVirtualMachines/openjdk-17.0.1/Contents/Home
pid=$(ps -ef | grep 'tcpServer-0.0.1.jar' | grep -v 'grep' | awk 'BEGIN{ FS=" "}; {print $2}')
if [ -n "$pid" ]; then
  echo "==============================================================="
  echo "PID : $pid, TCP 서버 [tcpServer-0.0.1.jar] 중지"
  echo "==============================================================="
	kill -9 $pid
fi
RELATIVE_DIR=`dirname "$0"`
SH_PATH=`readlink -f $RELATIVE_DIR`
nohup $_TCP_SERVER_JAVA_HOME'/bin/java' -jar -Xms128M -Xmx256M $SH_PATH'/tcpServer-0.0.1.jar' 1>/dev/null 2>&1 &
echo "==============================================================="
echo "TCP 서버 [tcpServer-0.0.1.jar] 가동 완료"
echo "==============================================================="