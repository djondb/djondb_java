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
ant
 
