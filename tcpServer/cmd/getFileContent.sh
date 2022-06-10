#! /bin/sh
if [ $# -eq 3 ]; then
	find $1 -name "$2" | xargs grep "$3"
else
	echo "파라미터가 부족합니다."
fi