@echo off

IF NOT "%1" == "" (
	
	IF NOT "%2" == ""  (
		
		IF NOT "%3" == ""  (
			
			cd %1
			dir /b | find /i "%2" > getFileContentList.txt
			for /f %%a in (getFileContentList.txt) do find "%3" %%a
			del getFileContentList.txt
				
		) ELSE (
			
			cd %1
			dir /b | find /i "%2"
			
		)
		
	) ELSE (
	
		cd %1
		dir /b
	)
	
) ELSE (

	echo "경로 파라미터가 없습니다."
)