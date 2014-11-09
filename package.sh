#!/bin/bash

SCRIPT=`realpath -s $0`
WORK_DIR=`dirname $SCRIPT`
TMP_DIR=`mktemp -d`

if [ ! -f $WORK_DIR/build/libs/glacier-interface.jar ]; then
	echo "Cannot find main jar file"
	exit -1;
fi;

# Create a copy of the package folder 
cp -R $WORK_DIR/debian $TMP_DIR/DEBIAN

# Copy the built files to the file
mkdir -p $TMP_DIR/usr/share/glacier-interface
mkdir -p $TMP_DIR/usr/bin
cp $WORK_DIR/build/libs/glacier-interface.jar $TMP_DIR/usr/share/glacier-interface
cp $WORK_DIR/gi $TMP_DIR/usr/bin

# Package 
cd $TMP_DIR
fakeroot dpkg-deb --build $TMP_DIR

# Clean 
#rm -rf $TMP_DIR

exit 0