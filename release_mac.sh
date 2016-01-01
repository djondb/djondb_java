#!/bin/sh

echo "<<<<<  Executing cmake  >>>>>"

cd native
rm -rf build
mkdir build
cd build
cmake ..
make

cd ../../java
ant
 
