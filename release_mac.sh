#!/bin/sh

echo "<<<<<  Executing cmake  >>>>>"

sh update.sh
cd native
rm -rf build
mkdir build
cd build
cmake ..
make

cd ../../java
ant clean
ant
 
cd ..
rm -rf dist
mkdir dist
jarfile="djondb_client_`uname`_`uname -m`${SUFFIX}.jar"
mv java/dist/lib/djondb_java.jar dist/$jarfile
