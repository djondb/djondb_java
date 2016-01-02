
@rem Capture startup path
@SET PATH_SRC_STARTUP=%~dp0

@rem Set source path based on current path
@pushd "%CD%"
@CD /D "%PATH_SRC_STARTUP%..\..\.."

@popd

@rem Applications Paths
@rem @set VS=VS2008

if "%VS%" == "VS2005" (
@set PATH_MSBUILD=C:\Windows\Microsoft.NET\Framework\v2.0.50727
@set PATH_MSBUILD_x64=C:\Windows\Microsoft.NET\Framework64\v2.0.50727
)
if "%VS%" == "VS2008" (
@set PATH_MSBUILD=C:\Windows\Microsoft.NET\Framework\v3.5
@set PATH_MSBUILD_x64=C:\Windows\Microsoft.NET\Framework64\v3.5
)
if "%VS%" == "" (
@SET PATH_MSBUILD=C:\WINDOWS\Microsoft.NET\Framework\v4.0.30319
@SET PATH_MSBUILD_x64=C:\WINDOWS\Microsoft.NET\Framework64\v4.0.30319
)

@echo source dir: %PATH_SRC_STARTUP%

@set INNO_PATH=c:\Program Files (x86)\Inno Setup 5
@set ANT_PATH=C:\development\Java\apache-ant-1.8.4
@SET OUTPUTDIR=%PATH_SRC_STARTUP%\windows\output


