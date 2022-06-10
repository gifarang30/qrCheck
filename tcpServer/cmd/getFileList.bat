@echo off

IF NOT "%1" == "" (
	IF NOT "%2" == ""  (
		cd %1
		dir /b | find /i "%2"
	) ELSE (
		cd %1
		dir /b
	)
) ELSE (
	echo "경로 파라미터가 없습니다."
)