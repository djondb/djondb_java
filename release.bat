@echo off
setlocal enabledelayedexpansion

if [%1] ==[] goto usage


call update.bat

call setenv.bat
call:parseArguments %*

:Win32
if "%x32%" == "true" (
	echo starting win32
	if "%VS%" == "" (
		set GEN_NAME="Visual Studio 10"
	)
	if "%VS%" == "VS2008" (
		set GEN_NAME="Visual Studio 9 2008"
	)
	SET PLATFORM=Win32
	SET NEXT_STEP=x64
	GOTO ExecuteJob
)

:x64
if "%x64%" == "true" (
	echo starting x64
	if "%VS%" == "" (
		set GEN_NAME="Visual Studio 10 Win64"
	)
	if "%VS%" == "VS2008" (
		set GEN_NAME="Visual Studio 9 2008 Win64"
	)
	SET PLATFORM=x64
	SET NEXT_STEP=END
	GOTO ExecuteJob
)

GOTO END

:ExecuteJob
cd native
rmdir /S /Q build
mkdir build
cd build
cmake .. -G %GEN_NAME% -DCMAKE_TARGET=%PLATFORM%
set Configuration=RelWithDebInfo
%PATH_MSBUILD%\msbuild djonjavadriver.sln /p:Configuration=%Configuration% /p:Platform=%PLATFORM%

cd ..\..\java

call ant clean
call ant

cd ..

@rem rmdir /S /Q dist
@rem mkdir dist
set jarfile="djondb_client_Windows_%PLATFORM%.jar"
move java\dist\lib\djondb_java.jar dist\%jarfile%

GOTO END

:usage

Echo Usage: release [-x32] [-x64]
goto exit

:END

Echo Done!

goto Exit

:getArg
set valname=%~1
echo arg: !%valname%!
goto:eof

:parseArguments
rem ----------------------------------------------------------------------------------
@echo off

:loop
IF "%~1"=="" GOTO cont

set argname=%~1
set argname=%argname:~1,100%
set value=%~2

@rem if the next value starts with - then it's a new parameter
if "%value:~0,1%" == "-" (
   set !argname!=true
   SHIFT & GOTO loop
)

if "%value%" == "" (
   set !argname!=true
   SHIFT & GOTO loop
)

set !argname!=%~2

@rem jumps first and second parameter
SHIFT & SHIFT & GOTO loop

:cont

goto:eof
rem ----------------------------------------------------------------------------------

:Exit
