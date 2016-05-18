cmake_minimum_required (VERSION 2.8.11)
project (djonjavadriver)

SET(CMAKE_MACOSX_RPATH ON)

IF ("${CMAKE_SYSTEM_NAME}" MATCHES "Linux")
	set(LINUX true)
	set(LIBRARY_PREFIX "${PROJECT_SOURCE_DIR}/libs/lib")
	set(LIBRARY_SUFFIX ".a")
ENDIF ("${CMAKE_SYSTEM_NAME}" MATCHES "Linux")


IF ("${CMAKE_SYSTEM_NAME}" MATCHES "Windows")
	set(WIN32 true)

	#set(LIBRARY_PREFIX "${PROJECT_SOURCE_DIR}/third_party/libs/${PLATFORM_NAME}/${CMAKE_BUILD_TYPE}")

	set(LIBRARY_SUFFIX ".lib")

	set(TARGET "")
	IF( CMAKE_TARGET STREQUAL "Win32" )
		message("Target is Win32")
		set(TARGET ${CMAKE_TARGET})
		set(LIBRARY_PREFIX "${PROJECT_SOURCE_DIR}/libs/x32/")
		set(CMAKE_MSVC_ARCH x86)
	ENDIF( CMAKE_TARGET STREQUAL "Win32" )
	IF( CMAKE_TARGET STREQUAL "x64" )
		message("Target is x64")
		set(TARGET ${CMAKE_TARGET})
		set(LIBRARY_PREFIX "${PROJECT_SOURCE_DIR}/libs/x64/")
		set(CMAKE_MSVC_ARCH amd64)
	ENDIF( CMAKE_TARGET STREQUAL "x64" )
	IF( "${TARGET}" STREQUAL "" )
		message(SEND_ERROR "Error you didn't specify CMAKE_TARGET, please use -DCMAKE_TARGET=(Win32|x64)")
	ENDIF( "${TARGET}" STREQUAL "" )

	message( STATUS " Libraries ${CMAKE_INSTALL_SYSTEM_RUNTIME_LIBS}")
	# Try and find the vcredist_XX.exe, normally this is in the WindowsSDK folder.
	#if( MSVC10 )
		find_program(MSVC_REDIST NAMES VC/vcredist_${CMAKE_MSVC_ARCH}.exe
			PATHS
			"[HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Microsoft SDKs\\Windows\\v7.0A;InstallationFolder]/Redist/"               
			)
		get_filename_component(vcredist_name "${MSVC_REDIST}" NAME)
		#endif( MSVC10 )

		message( STATUS "vcre ${vsredist_name}")
	# If we found a vcredist-package, we add it simply to the 
	# installation-folder and run it with NSis.
	if( vcredist_name )
		message( STATUS "    Adding " ${vcredist_name} " to Install" )
		install(PROGRAMS ${MSVC_REDIST} COMPONENT client,server,service,development DESTINATION bin)
		# Add /q to make the vcredist install silent
		set(CPACK_NSIS_EXTRA_INSTALL_COMMANDS "ExecWait '\\\"$INSTDIR\\\\bin\\\\${vcredist_name}\\\" /q'" )
	endif( vcredist_name )
ENDIF ("${CMAKE_SYSTEM_NAME}" MATCHES "Windows")

message( STATUS "System Name " ${CMAKE_SYSTEM_NAME} )
IF ("${CMAKE_SYSTEM_NAME}" MATCHES "Darwin")
	set(LIBRARY_PREFIX "${PROJECT_SOURCE_DIR}/libs/lib")
	set(LIBRARY_SUFFIX ".a")
	set(DARWIN true)
	#add_definitions(-arch x86_64 -fPIC -std=c99)
	add_definitions(-arch x86_64 -fPIC)
	set(CMAKE_CXX_LDFLAGS "${CMAKE_CXX_LDFLAGS} -v -lstdc++.6")
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_LDFLAGS} -v -lstdc++.6")

	SET(CMAKE_MACOSX_RPATH ON)

	# use, i.e. don't skip the full RPATH for the build tree
	SET(CMAKE_SKIP_BUILD_RPATH  FALSE)

	# when building, don't use the install RPATH already
	# (but later on when installing)
	SET(CMAKE_BUILD_WITH_INSTALL_RPATH FALSE) 

	SET(CMAKE_INSTALL_RPATH "${CMAKE_INSTALL_PREFIX}/lib")

	# add the automatically determined parts of the RPATH
	# which point to directories outside the build tree to the install RPATH
	SET(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)


	# the RPATH to be used when installing, but only if it's not a system directory
	LIST(FIND CMAKE_PLATFORM_IMPLICIT_LINK_DIRECTORIES "${CMAKE_INSTALL_PREFIX}/lib" isSystemDir)
	IF("${isSystemDir}" STREQUAL "-1")
		SET(CMAKE_INSTALL_RPATH "${CMAKE_INSTALL_PREFIX}/lib")
	ENDIF("${isSystemDir}" STREQUAL "-1")

ENDIF ("${CMAKE_SYSTEM_NAME}" MATCHES "Darwin")

include_directories("${PROJECT_SOURCE_DIR}/includes")

IF (NOT WIN32)
	add_library(djonjavadriver SHARED javadriver_wrap.cpp jniutils.cpp)
ENDIF (NOT WIN32)
IF (WIN32)
	add_library(djonjavadriver SHARED javadriver_wrap.cpp jniutils.cpp)
ENDIF (WIN32)

IF (LINUX)
	target_link_libraries (djonjavadriver ${LIBRARY_PREFIX}djon-clientStatic${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-tx${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-network${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-command${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-db${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-filesystem${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-cache${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-bson${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-util${LIBRARY_SUFFIX}  ${LIBRARY_PREFIX}antlr3c${LIBRARY_SUFFIX} pthread uuid rt)
ENDIF (LINUX)
IF (WIN32)
	target_link_libraries (djonjavadriver ${LIBRARY_PREFIX}djon-client${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-tx${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-network${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-command${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-db${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-filesystem${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-cache${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-bson${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-util${LIBRARY_SUFFIX}  ${LIBRARY_PREFIX}antlr3c${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}pthreadVC2${LIBRARY_SUFFIX} Rpcrt4.lib)
ENDIF (WIN32)
IF (DARWIN)
	target_link_libraries (djonjavadriver ${LIBRARY_PREFIX}djon-clientStatic${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-tx${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-network${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-command${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-db${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-filesystem${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-cache${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-bson${LIBRARY_SUFFIX} ${LIBRARY_PREFIX}djon-util${LIBRARY_SUFFIX}  ${LIBRARY_PREFIX}antlr3c${LIBRARY_SUFFIX} pthread boost_system boost_regex boost_date_time)
ENDIF (DARWIN)
