#! /bin/sh
if [ $# -ge 2 ]; then
	find $1 -name "$2"
else
	echo "파라미터가 부족합니다."
fi