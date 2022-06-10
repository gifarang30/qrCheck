#! /bin/sh
now=$(date +"%Y-%m-%d")
tail -f /home/netty_tcp/logs/$(date +"%Y%m")/kjb_netty_tcp_$(date +"%Y%m%d").0.log
if [ $# -ge 1 ]; then

else
	echo "파라미터가 부족합니다."
fi