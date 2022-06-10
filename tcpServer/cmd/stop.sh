#! /bin/sh
pid=$(ps -ef | grep 'tcpServer-0.0.1.jar' | grep -v 'grep' | awk 'BEGIN{ FS=" "}; {print $2}')
if [ -n "$pid" ]; then 
	kill -9 $pid
	echo "==============================================================="
  echo "PID : $pid, TCP 서버 [tcpServer-0.0.1.jar] 중지"
  echo "==============================================================="
fi