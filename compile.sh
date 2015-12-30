#!/bin/sh
JAVA=$JAVA_HOME
while getopts s:j:d:o: o
   do case "$o" in
		j)  JAVA="$OPTARG";;
		d)  DIR="$OPTARG";;
	   s)  SUFFIX="$OPTARG";;
		\?)  echo "Usage: $0 -j <java_home> [-s suffix] [-d outputdir]" && exit 1;;
	esac
done

if [ -z "${JAVA}" ]; 
then
	echo "You will need to specify the JDK home directory" 
   echo "Usage: $0 -j <java_home> [-s suffix] [-d outputdir]"
	exit 1
fi

export JAVA_HOME=${JAVA}

echo "native JNI compilation"

cd native

echo "<<<<<  Executing autoreconf  >>>>>"

autoreconf --install --force

rm -rf obj
mkdir obj
cd obj
../configure --prefix=/usr
make
make DESTDIR=`pwd` install

cd ../../java
ant clean
ant

cd ..
rm -rf dist
mkdir dist
jarfile="djondb_client_`uname`_`uname -m`${SUFFIX}.jar"
mv java/dist/lib/djondb_java.jar dist/$jarfile
#scp dist/$jarfile  crossleyjuan@djondb.com:html/downloads/$jarfile

